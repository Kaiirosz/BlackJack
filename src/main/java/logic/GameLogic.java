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
    private final GameUtils utils;

    public GameLogic(GameIO io){
        dealer = new Dealer(new Deck());
        utils = new GameUtils();
        this.io = io;
    }

    public void start(){
        io.println("Blackjack");
        io.println("Enter your name to start the game.");
        String playerName = io.readLine();
        player = new Player(playerName, 250000);
        io.println("Game Start");
        io.println("--------------");
        do {
            int betMoney = betMoney();
            Outcome outcome = startRound();
            dealer.returnCardsToDeck(player.getAllCards());
            int betResult = processOutcome(outcome, betMoney);
            player.setMoney(player.getMoney() + betResult);
        }
        while (player.getMoney() > 0);

    }

    public int betMoney(){
        int playerMoney = player.getMoney();
        int betMoney;
        while (true) {
            io.println("Enter the amount of money you want to bet:");
            io.printMoney(playerMoney);
            betMoney = io.readInt() * 100;
            if (betMoney > playerMoney || betMoney < 0) {
                io.println("Invalid amount of money, Try Again.");
                continue;
            }
            player.setMoney(playerMoney - betMoney);
            io.println(betMoney + " was put into the bet.");
            io.printMoney(player.getMoney());
            break;
        }
        return betMoney;
    }

    public Outcome startRound(){
        io.println("--------------");
        io.println("Round Started");
        dealerDeals();
        utils.pauseForEffect(2000);
        if (checkForPlayerBlackjack()){
            if (checkForDealerBlackjack()){
                return Outcome.PUSH;
            }
            return Outcome.BLACKJACK;
        }
        printCards();
        return playerTurn();
    }

    public Outcome playerTurn(){
        while (true) {
            io.println("What do you do now?");
            io.println("1. Hit\n2. Stand");
            String action = io.readLine();
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

    public void playerHits(){
        Card cardHit = dealer.giveCard();
        io.println("The Dealer gave you a card...");
        utils.pauseForEffect(1000);
        io.println("It's a " + cardHit.getCardNotation() + "!");
        utils.pauseForEffect(1000);
        player.addCardToHand(cardHit);
        printCards();
    }

    public Outcome playerStands(){
        return dealerTurn();
    }

    public void dealerDeals(){
        io.println("The Dealer is shuffling the deck");
        dealer.shuffleDeck();
        utils.pauseForEffect(1000);
        io.println("The cards are being dealt");
        utils.pauseForEffect(1000);
        dealer.dealCards(player);
        List<Card> playerCards = player.getAllCards();
        io.println("You got the card " + playerCards.getFirst().getCardNotation() + "!");
        utils.pauseForEffect(1000);
        io.println("You got the card " + playerCards.get(1).getCardNotation() + "!");
        utils.pauseForEffect(1000);
        io.println("The Dealer's face up card is " + dealer.getFaceUpCardNotation());
    }

    public boolean checkForPlayerBlackjack(){
        if (player.getHandTotalBlackJackValue() == 21){
            io.println("BLACKJACK!BLACKJACK!BLACKJACK!");
            return true;
        }
        return false;
    }

    public boolean checkForDealerBlackjack(){
        if (dealer.getTotalBlackJackValue() == 21){
            utils.pauseForEffect(2000);
            io.println("......");
            io.println("The Dealer reveals their face down card!");
            utils.pauseForEffect(1000);
            io.println("The Dealers cards are: [" + dealer.getFaceUpCardNotation() + ", " + dealer.getFaceDownCardNotation() + "]!!");
            return true;
        }
        return false;
    }

    public Outcome dealerTurn(){
        io.println("The Dealer is revealing their face down card......");
        utils.pauseForEffect(3000);
        io.println("It's a " + dealer.getFaceDownCardNotation() + "!");
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue == 21){
            return Outcome.LOSE;
        }
        while (dealersTotalValue < 17){
            utils.pauseForEffect(1000);
            io.println("The Dealer has decided to hit for another card");
            dealer.hit();
            utils.pauseForEffect(2000);
            io.println("It's a " + dealer.getLastCardNotation() + "!");
            dealersTotalValue = dealer.getTotalBlackJackValue();
            io.println("Dealer's Value: " + dealersTotalValue);
        }
        return compareTotalBlackJackValues(dealersTotalValue);
    }

    public Outcome compareTotalBlackJackValues(int dealersTotalValue){
        if (dealersTotalValue > 21){
            io.println("The Dealer busts!!");
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

    public void printCards(){
        io.println("--------------");
        io.println("Your cards: " + player.getDisplayedHand() + " = " + player.getHandTotalBlackJackValue());
        io.println("The Dealer's cards: " + dealer.getCards());
    }

    public int processOutcome(Outcome outcome, int betMoney){
        utils.pauseForEffect(1500);
        BigDecimal multiplier = BigDecimal.valueOf(outcome.getMultiplier());
        BigDecimal winnings = multiplier.multiply(BigDecimal.valueOf(betMoney));
        betMoney = winnings.intValue();
        if (outcome.equals(Outcome.BLACKJACK)){
            io.println("You won " + betMoney / 100 + "!!");
        }
        else if (outcome.equals(Outcome.PUSH)){
            io.println("Push");
            io.println("Bet money returned");
        }
        else if (outcome.equals(Outcome.WIN)){
            io.println("You win!!");
            io.println("Winnings: " + betMoney / 100);
        }
        else if (outcome.equals(Outcome.LOSE)){
            io.println("You lose!!");
        }
        else if (outcome.equals(Outcome.BUST)){
            io.println("Bust!!");
            io.println("You lose!!");
        }
        return betMoney;
    }


}
