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
    private RoundOutcome roundOutcome;
    private final GameIO io;
    private final GameUtils utils;

    public RoundHandler(GameContext gameContext, GameIO io, GameUtils utils) {
        this.io = io;
        this.playerManager = playerManager;
        this.betManager = betManager;
        this.dealer = dealer;
        this.utils = utils;
    }


    public RoundOutcome startRound() {
        io.displayRoundStart();
        dealerDeals();
        Optional<RoundOutcome> bjOutcome = handleBlackjackPhase();
        if (bjOutcome.isPresent()){
            return bjOutcome.get();
        }
        io.printAllCards(playerManager, dealer);
        humanPlayerTurn(playerManager.getHumanPlayer());
        for (Player ai: playerManager.getAIPlayers()){
            playAITurn(ai);
        }
        dealer.returnCardsToDeck(player.getAllCards());
        roundOutcome = new RoundOutcome(playerOutcomeMap);
        return roundOutcome;
    }

    private void dealerDeals() {
        io.showDealerIsShufflingTheDeckMessage();
        dealer.shuffleDeck();
        utils.pauseForEffect(1000);
        io.showCardsDealtMessage();
        utils.pauseForEffect(1000);
        dealer.dealCards(playerManager);
        for (Player p : playerManager.getAllPlayers()) {
            Card firstCard = p.getFirstHand().getCard(0);
            Card secondCard = p.getFirstHand().getCard(1);
            io.printAcquiredCardNotification(firstCard, secondCard, p);
            utils.pauseForEffect(1000);
        }
        io.printDealersFaceUpCardNotification(dealer.getFaceUpCard());
        utils.pauseForEffect(1000);
    }

    private Optional<RoundOutcome> handleBlackjackPhase(){
        BlackjackEvaluator blackjackEvaluator = new BlackjackEvaluator(ga);
        if (checkForPlayerBlackjack(blackjackEvaluator)){
            roundOutcome = blackjackEvaluator.settleBlackJack();
            if (checkForDealerBlackjack(blackjackEvaluator)){
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

    private boolean checkForDealerBlackjack(BlackjackEvaluator blackjackEvaluator){
        boolean dealerHasBlackjack = checkForPlayerBlackjack(blackjackEvaluator);
        if (dealerHasBlackjack){
            io.showDealerRevealingCardMessage();
            utils.pauseForEffect(1000);
            io.printDealersBlackjackPair(dealer.getFaceUpCard(), dealer.getFaceDownCard());
        }
        return dealerHasBlackjack;
    }


    private void humanPlayerTurn(Player player) {
        while (true) {
            String action = askForPlayerAction();
            if (action.equalsIgnoreCase("Hit")) {
                playerHits();
                if (player.getHandTotalBlackJackValue() > 21) {
                    playerOutcomeMap.put(player, Outcome.BUST);
                    playerManager.removePlayerFromRound(player);
                    return;
                } else if (player.getHandTotalBlackJackValue() == 21) {
                    return;
                }
            } else if (action.equalsIgnoreCase("Stand")) {
                return;
            } else {
                io.displayUnknownActionMessage();
            }
        }
    }

    private String askForPlayerAction() {
        io.displayPlayerOptions();
        return io.readLine();
    }

    private void playerHits() {
        Card cardHit = dealer.giveCard();
        io.showDealerGivingCardMessage();
        utils.pauseForEffect(1000);
        io.printRevealedCardNotification(cardHit);
        utils.pauseForEffect(1000);
        Player humanPlayer = playerManager.getHumanPlayer();
        humanPlayer.addCardToHand(cardHit);
        io.printEveryoneCards(playerManager, dealer);
    }

    private void playerStands() {
        dealerTurn();
    }

    private void playAITurn(Player ai){
        int totalBlackjackValue = ai.getHandTotalBlackJackValue();
        if (totalBlackjackValue )
    }

    private Outcome dealerTurn() {
        displayDealerInitialCardReveal();
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue == 21) {
            return Outcome.LOSE;
        }
        dealersTotalValue = dealerHitsTillSeventeen(dealersTotalValue);
        return compareTotalBlackJackValues(dealersTotalValue);
    }

    private void displayDealerInitialCardReveal() {
        io.showDealerRevealingCardMessage();
        utils.pauseForEffect(3000);
        io.printRevealedCardNotification(dealer.getFaceDownCard());
    }

    private int dealerHitsTillSeventeen(int dealersTotalValue) {
        while (dealersTotalValue < 17) {
            utils.pauseForEffect(1000);
            io.showDealerHitsMessage();
            dealer.hit();
            utils.pauseForEffect(2000);
            io.printRevealedCardNotification(dealer.getLastCard());
            dealersTotalValue = dealer.getTotalBlackJackValue();
            io.printDealersTotalValue(dealersTotalValue);
        }
        return dealersTotalValue;
    }

    private Outcome compareTotalBlackJackValues(int dealersTotalValue) {
        if (dealersTotalValue > 21) {
            io.printDealerBustsMessage();
            return Outcome.WIN;
        }
        int playersTotalValue = player.getHandTotalBlackJackValue();
        if (playersTotalValue == dealersTotalValue) {
            return Outcome.PUSH;
        }
        if (playersTotalValue > dealersTotalValue) {
            return Outcome.WIN;
        }
        return Outcome.LOSE;
    }
}
