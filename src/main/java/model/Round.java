package model;

import io.GameIO;
import logic.BlackjackEvaluator;
import logic.PlayerManager;
import utils.GameUtils;

import java.util.*;

public class Round {
    private final GameIO io;
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final GameUtils utils;
    private Map<Player, Outcome> playerOutcomeMap;

    public Round(GameIO io, PlayerManager playerManager, Dealer dealer, GameUtils utils) {
        this.io = io;
        this.playerManager = playerManager;
        this.dealer = dealer;
        this.utils = utils;
        playerOutcomeMap = new HashMap<>();
    }


    public RoundOutcome startRound() {
        RoundOutcome roundOutcome;
        io.displayRoundStart();
        dealerDeals();
        BlackjackEvaluator blackjackEvaluator = new BlackjackEvaluator(playerManager, dealer);
        List<Player> playersWithBJ = blackjackEvaluator.getPlayersWithBlackjack();
        if (!playersWithBJ.isEmpty()) {
            for (Player p: playersWithBJ){
                io.printBlackjackNotification(p);
                utils.pauseForEffect(1000);
            }
            playerOutcomeMap = blackjackEvaluator.settleBlackJack(playerOutcomeMap);
            if (blackjackEvaluator.checkForDealerBlackjack()){
                io.showDealerRevealingCardMessage();
                utils.pauseForEffect(1000);
                io.printDealersBlackjackPair(dealer.getFaceUpCard(), dealer.getFaceDownCard());
                return roundOutcome = new RoundOutcome(playerOutcomeMap);
            }
        }
        io.printEveryoneCards(playerManager, dealer);
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
            Card firstCard = p.getAllCards().getFirst();
            Card secondCard = p.getAllCards().get(1);
            io.printAcquiredCardNotification(firstCard, secondCard, p);
            utils.pauseForEffect(1000);
        }
        io.printDealersFaceUpCardNotification(dealer.getFaceUpCard());
        utils.pauseForEffect(1000);
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
