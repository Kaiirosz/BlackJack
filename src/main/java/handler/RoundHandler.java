package handler;

import io.GameIO;
import logic.*;
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
        handleInsuranceIfPossible();
        Optional<RoundOutcome> bjOutcome = handleBlackjackPhase();
        resolveInsurance();
        if (bjOutcome.isPresent()) {
            return bjOutcome.get();
        }
        io.printAllCards(playerManager, dealer);
        if (playerManager.checkIfHumanIsInRound()) {
            humanPlayerTurn();
        }
        if (playerManager.checkIfAnAIStillInRound()) {
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

    private void handleInsuranceIfPossible() {
        if (dealer.hasFaceUpAce()) {
            handlePlayerInsurance();
            handleAIInsurance();
        }
    }

    private void handlePlayerInsurance() {
        Player humanPlayer = playerManager.getHumanPlayer();
        if (humanPlayer.canAffordHalfOriginalBet()) {
            int halfBet = humanPlayer.getHalfOriginalBet();
            boolean didInsurance = io.askForInsurance(halfBet);
            if (didInsurance) {
                betManager.placeInsuranceBet(humanPlayer, halfBet);
            }
        }
    }

    private void handleAIInsurance() {
        AIInsuranceLogic aiInsuranceLogic = new AIInsuranceLogic();
        for (Player ai : playerManager.getAIPlayersInRound()) {
            if (ai.canAffordHalfOriginalBet() && aiInsuranceLogic.decideToInsure(ai)) {
                io.printAIInsuresNotification(ai.getName());
                int halfBet = ai.getHalfOriginalBet();
                betManager.placeInsuranceBet(ai, halfBet);
            }
        }
    }


    private Optional<RoundOutcome> handleBlackjackPhase() { //issue with bj peeking
        BlackjackEvaluator blackjackEvaluator = new BlackjackEvaluator(gameContext);
        roundOutcome = blackjackEvaluator.settleBlackJack();
        checkForPlayerBlackjack(blackjackEvaluator);
        if (checkForDealerBlackjack(blackjackEvaluator)) {
            return Optional.of(roundOutcome);
        }
        return Optional.empty();
    }

    private void checkForPlayerBlackjack(BlackjackEvaluator blackjackEvaluator) {
        List<Player> playersWithBJ = blackjackEvaluator.getPlayersWithBlackjack();
        if (!playersWithBJ.isEmpty()) {
            for (Player p : playersWithBJ) {
                io.printBlackjackNotification(p);
                utils.pauseForEffect(1000);
                playerManager.removePlayerFromRound(p);
            }
        }
    }

    private boolean checkForDealerBlackjack(BlackjackEvaluator blackjackEvaluator) {
        utils.pauseForEffect(500);
        boolean dealerHasBlackjack = blackjackEvaluator.checkForDealerBlackjack();
        io.showDealerPeeksMessage();
        utils.pauseForEffect(500);
        if (dealerHasBlackjack) {
            io.showDealerRevealingCardMessage();
            utils.pauseForEffect(1000);
            io.printDealersBlackjackPair(dealer.getFaceUpCard(), dealer.getFaceDownCard());
            utils.pauseForEffect(1000);
        }
        return dealerHasBlackjack;
    }

    private void resolveInsurance(){
        utils.pauseForEffect(1000);
        if (betManager.aPlayerInsured()) {
            io.showInsuranceResolvedMessage();
            betManager.clearInsuredPlayersList();
        }
    }

    private void humanPlayerTurn() {
        PlayerTurnHandler playerTurnHandler = new PlayerTurnHandler(gameContext, roundOutcome, io, utils);
        playerTurnHandler.handleTurn();
    }

    private void aiPlayersTurn() {
        AITurnHandler aiTurnHandler = new AITurnHandler(gameContext, roundOutcome, io, utils);
        aiTurnHandler.handleTurn();
    }


    private void dealerTurn() {
        DealerTurnHandler dealerTurnHandler = new DealerTurnHandler(gameContext, roundOutcome, io, utils);
        dealerTurnHandler.handleTurn();
    }

    private void settleRound() {
        utils.pauseForEffect(1000);
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue > 21) {
            io.printDealerBustsMessage();
            for (Player p : playerManager.getAllPlayersInRound()) {
                int betResult = 0;
                for (Hand hand : p.getHandList()) {
                    if (hand.getHandOutcome() != null) {
                        continue;
                    }
                    hand.setHandOutcome(Outcome.WIN);
                    int handBet = hand.getBet();
                    betResult += betManager.settleBetOutcome(hand.getHandOutcome(), handBet);
                }
                roundOutcome.addPlayerOutcome(p, betResult);
            }
            return;
        }
        for (Player p : playerManager.getAllPlayersInRound()) {
            int betResult = 0;
            for (Hand hand : p.getHandList()) {
                if (hand.getHandOutcome() != null) {
                    continue;
                }
                int handBet = hand.getBet();
                int handTotalValue = p.getHandTotalBlackJackValue(hand);
                if (handTotalValue == dealersTotalValue) {
                    hand.setHandOutcome(Outcome.PUSH);
                } else if (handTotalValue < dealersTotalValue) {
                    hand.setHandOutcome(Outcome.LOSE);
                } else {
                    hand.setHandOutcome(Outcome.WIN);
                }
                betResult += betManager.settleBetOutcome(hand.getHandOutcome(), handBet);
            }
            roundOutcome.addPlayerOutcome(p, betResult);
        }
    }
}
