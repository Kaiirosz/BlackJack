package model;

import io.GameIO;
import utils.GameUtils;

import java.util.List;

public class Round {
    private final GameIO io;
    private final Player player;
    private final Dealer dealer;
    private final GameUtils utils;

    public Round(GameIO io, Player player, Dealer dealer, GameUtils utils){
        this.io = io;
        this.player = player;
        this.dealer = dealer;
        this.utils = utils;
    }


    public Outcome startRound(){
        io.displayRoundStart();
        dealerDeals();
        if (checkForPlayerBlackjack()){
            return settleBlackJack();
        }
        io.printPlayerCards(player,dealer);
        Outcome roundOutcome =  playerTurn();
        dealer.returnCardsToDeck(player.getAllCards());
        return roundOutcome;
    }

    private void dealerDeals(){
        io.showDealerIsShufflingTheDeckMessage();
        dealer.shuffleDeck();
        utils.pauseForEffect(1000);
        io.showCardsDealtMessage();
        utils.pauseForEffect(1000);
        dealer.dealCards(player);
        List<Card> playerCards = player.getAllCards();
        io.printAcquiredCardNotification(playerCards.getFirst());
        utils.pauseForEffect(1000);
        io.printAcquiredCardNotification(playerCards.get(1));
        utils.pauseForEffect(1000);
        io.printDealersFaceUpCardNotification(dealer.getFaceUpCard());
        utils.pauseForEffect(1000);
    }

    private boolean checkForPlayerBlackjack(){
        if (player.getHandTotalBlackJackValue() == 21){
            io.printBlackjack();
            return true;
        }
        return false;
    }

    private Outcome settleBlackJack(){
        if (dealer.getTotalBlackJackValue() == 21){
            utils.pauseForEffect(2000);
            io.showDealerRevealingCardMessage();
            utils.pauseForEffect(1000);
            io.printDealersBlackjackPair(dealer.getFaceUpCard(), dealer.getFaceDownCard());
            return Outcome.PUSH;
        }
        return Outcome.BLACKJACK;
    }

    private Outcome playerTurn(){
        while (true) {
            String action = askForPlayerAction();
            if (action.equalsIgnoreCase("Hit")) {
                playerHits();
                if (player.getHandTotalBlackJackValue() > 21) {
                    return Outcome.BUST;
                }
                else if (player.getHandTotalBlackJackValue() == 21){
                    return playerStands();
                }
            }
            else if (action.equalsIgnoreCase("Stand")) {
                return playerStands();
            }
            else {
                io.println("Unknown Action. Try Again.");
            }
        }
    }

    private String askForPlayerAction(){
        io.displayPlayerOptions();
        return io.readLine();
    }

    private void playerHits(){
        Card cardHit = dealer.giveCard();
        io.showDealerGivingCardMessage();
        utils.pauseForEffect(1000);
        io.printRevealedCardNotification(cardHit);
        utils.pauseForEffect(1000);
        player.addCardToHand(cardHit);
        io.printPlayerCards(player, dealer);
    }

    private Outcome playerStands(){
        return dealerTurn();
    }

    private Outcome dealerTurn(){
        displayDealerInitialCardReveal();
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue == 21){
            return Outcome.LOSE;
        }
        dealersTotalValue = dealerHitsTillSeventeen(dealersTotalValue);
        return compareTotalBlackJackValues(dealersTotalValue);
    }

    private void displayDealerInitialCardReveal(){
        io.showDealerRevealingCardMessage();
        utils.pauseForEffect(3000);
        io.printRevealedCardNotification(dealer.getFaceDownCard());
    }

    private int dealerHitsTillSeventeen(int dealersTotalValue){
        while (dealersTotalValue < 17){
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

    private Outcome compareTotalBlackJackValues(int dealersTotalValue){
        if (dealersTotalValue > 21){
            io.printDealerBustsMessage();
            return Outcome.WIN;
        }
        int playersTotalValue = player.getHandTotalBlackJackValue();
        if (playersTotalValue == dealersTotalValue){
            return Outcome.PUSH;
        }
        if (playersTotalValue > dealersTotalValue){
            return Outcome.WIN;
        }
        return Outcome.LOSE;
    }
}
