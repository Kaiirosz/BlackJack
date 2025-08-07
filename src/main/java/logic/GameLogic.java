package logic;

import io.GameIO;
import model.*;
import utils.GameUtils;

import java.math.BigDecimal;
import java.util.List;

public class GameLogic {
    private Player player;
    private final Dealer dealer;
    private final GameIO io;
    private final BetManager betManager;
    private final GameUtils utils;

    public GameLogic(GameIO io){
        dealer = new Dealer(new Deck());
        utils = new GameUtils();
        this.io = io;
        betManager = new BetManager(this.io);
    }

    public void start(){
        io.displayGameTitle();
        initializePlayer();
        io.displayGameStartBanner();
        continueGameUntilPlayerHasNoMoney();
    }

    private void initializePlayer(){
        String playerName = io.askForPlayerName();
        player = new Player(playerName, 250000);
    }

    private void continueGameUntilPlayerHasNoMoney(){
        do {
            int betMoney = betMoney();
            Outcome outcome = startRound();
            dealer.returnCardsToDeck(player.getAllCards());
            int betResult = processOutcome(outcome, betMoney);
            RoundResult roundResult = new RoundResult(outcome, betResult);
            player.setMoney(player.getMoney() + betResult);
        }
        while (player.getMoney() > 0);
    }

    private int betMoney(){
        return betManager.askAndPlaceBet(player);
    }

    private Outcome startRound(){
        io.displayRoundStart();
        dealerDeals();
        if (checkForPlayerBlackjack()){
            return settleBlackJack();
        }
        io.printPlayerCards(player,dealer);
        return playerTurn();
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
        utils.pauseForEffect(2000);
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

    private int processOutcome(Outcome outcome, int betMoney){
        utils.pauseForEffect(1500);
        BigDecimal multiplier = BigDecimal.valueOf(2);
        BigDecimal winnings = multiplier.multiply(BigDecimal.valueOf(betMoney));
        betMoney = winnings.intValue();
        if (outcome.equals(Outcome.BLACKJACK)){
            io.println("You won " + betMoney / 100 + "!!");
        }
        else if (outcome.equals(Outcome.PUSH)){
            io.println("Push");
            io.println("Bet money returned.");
        }
        else if (outcome.equals(Outcome.WIN)){
            io.println("You win!");
            io.println("Winnings: " + betMoney / 100);
        }
        else if (outcome.equals(Outcome.LOSE)){
            io.println("You lose!");
        }
        else if (outcome.equals(Outcome.BUST)){
            io.println("Bust!");
            utils.pauseForEffect(1000);
            io.println("You lose!");
        }
        return betMoney;
    }
}
