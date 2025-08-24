package handler;

import io.GameIO;
import logic.BetManager;
import logic.BlackjackEvaluator;
import logic.GameContext;
import logic.PlayerManager;
import model.*;
import utils.GameUtils;

import java.util.*;

public class RoundHandler {

    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final GameContext gameContext;
    private RoundOutcome roundOutcome;
    private final GameIO io;
    private final GameUtils utils;

    public RoundHandler(GameContext gameContext, GameIO io, GameUtils utils) {
        this.gameContext = gameContext;
        this.playerManager = gameContext.getPlayerManager();
        this.betManager = gameContext.getBetManager();
        this.dealer = gameContext.getDealer();
        roundOutcome = new RoundOutcome();
        this.io = io;
        this.utils = utils;
    }

    public RoundOutcome startRound() {
        io.displayRoundStart();
        dealerDeals();
        Optional<RoundOutcome> bjOutcome = handleBlackjackPhase();
        if (bjOutcome.isPresent()) {
            return bjOutcome.get();
        }
        io.printAllCards(playerManager, dealer);
        if (playerManager.checkIfHumanIsInRound()){
            humanPlayerTurn();
        }
        if (playerManager.checkIfAnAIStillInRound()){
            aiPlayersTurn();
        }
        if (playerManager.getAllPlayersInRound().isEmpty()) {
            return roundOutcome;
        }
        dealerTurn();
        settleRound();
        return roundOutcome;
    }

    private void dealerDeals() {
        io.showDealerIsShufflingTheDeckMessage();
        dealer.shuffleDeck();
        utils.pauseForEffect(1000);
        io.showCardsDealtMessage();
        utils.pauseForEffect(1000);
        dealer.dealCards(playerManager);
        for (Player p : playerManager.getAllPlayersInRound()) {
            Card firstCard = p.getFirstHand().getCard(0);
            Card secondCard = p.getFirstHand().getCard(1);
            io.printAcquiredCardNotification(firstCard, secondCard, p);
            utils.pauseForEffect(1000);
        }
        io.printDealersFaceUpCardNotification(dealer.getFaceUpCard());
        utils.pauseForEffect(1000);
    }

    private Optional<RoundOutcome> handleBlackjackPhase() { //issue with bj peeking
        BlackjackEvaluator blackjackEvaluator = new BlackjackEvaluator(gameContext);
        if (checkForPlayerBlackjack(blackjackEvaluator)) {
            roundOutcome = blackjackEvaluator.settleBlackJack();
            for (Player p : blackjackEvaluator.getPlayersWithBlackjack()) {
                playerManager.removePlayerFromRound(p);
            }
            if (checkForDealerBlackjack(blackjackEvaluator)) {
                return Optional.of(roundOutcome);
            }
        }
        return Optional.empty();
    }

    private boolean checkForPlayerBlackjack(BlackjackEvaluator blackjackEvaluator) {
        boolean aPlayerHasBlackjack = false;
        List<Player> playersWithBJ = blackjackEvaluator.getPlayersWithBlackjack();
        if (!playersWithBJ.isEmpty()) {
            aPlayerHasBlackjack = true;
            for (Player p : playersWithBJ) {
                io.printBlackjackNotification(p);
                utils.pauseForEffect(1000);
            }
        }
        return aPlayerHasBlackjack;
    }

    private boolean checkForDealerBlackjack(BlackjackEvaluator blackjackEvaluator) {
        boolean dealerHasBlackjack = checkForPlayerBlackjack(blackjackEvaluator);
        if (dealerHasBlackjack) {
            io.showDealerRevealingCardMessage();
            utils.pauseForEffect(1000);
            io.printDealersBlackjackPair(dealer.getFaceUpCard(), dealer.getFaceDownCard());
        }
        return dealerHasBlackjack;
    }

    private void humanPlayerTurn() {
        PlayerTurnHandler playerTurnHandler = new PlayerTurnHandler();
        playerTurnHandler.handleTurn(gameContext, roundOutcome, io, utils);
    }

    private void aiPlayersTurn() {
        AITurnHandler aiTurnHandler = new AITurnHandler();
        aiTurnHandler.handleTurn(gameContext, roundOutcome, io, utils);
    }


    private void dealerTurn() {
        DealerTurnHandler dealerTurnHandler = new DealerTurnHandler();
        dealerTurnHandler.handleTurn(gameContext, roundOutcome, io, utils);
    }



    private void settleRound() {
        utils.pauseForEffect(1000);
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue > 21) {
            io.printDealerBustsMessage();
            for (Player p : playerManager.getAllPlayersInRound()) {
                Hand hand = p.getFirstHand();
                hand.setHandOutcome(Outcome.WIN);
                int playerBet = hand.getBet();
                int betResult = betManager.settleBetOutcome(hand.getHandOutcome(), playerBet);
                roundOutcome.addPlayerOutcome(p, betResult);
            }
            return;
        }
        for (Player p : playerManager.getAllPlayersInRound()) {
            Hand hand = p.getFirstHand();
            int playersTotalValue = p.getHandTotalBlackJackValue(hand);
            int playerBet = hand.getBet();
            int betResult;
            if (playersTotalValue == dealersTotalValue) {
                hand.setHandOutcome(Outcome.PUSH);
            } else if (playersTotalValue < dealersTotalValue) {
                hand.setHandOutcome(Outcome.LOSE);
            } else {
                hand.setHandOutcome(Outcome.WIN);
            }
            betResult = betManager.settleBetOutcome(hand.getHandOutcome(), playerBet);
            roundOutcome.addPlayerOutcome(p, betResult);
        }
    }
}
