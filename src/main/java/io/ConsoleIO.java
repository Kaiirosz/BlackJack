package io;

import model.Card;
import model.Dealer;
import model.Outcome;
import model.Player;
import java.util.Scanner;

public class ConsoleIO implements GameIO {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void printMoney(int money) {
        System.out.printf("Current Money: ₱%d%n", money / 100);
    }

    @Override
    public String readLine() {
        return sc.nextLine();
    }

    @Override
    public int readInt() {
        int input = sc.nextInt();
        sc.nextLine();
        return input;
    }

    @Override
    public void displayGameTitle() {
        println("Blackjack");
    }

    @Override
    public void displayGameStartBanner() {
        println("Game Start");
        println("--------------");
    }

    @Override
    public String askForPlayerName() {
        println("Enter your name to start the game");
        return sc.nextLine();
    }

    @Override
    public int askForBetAmount(int playerBalance) {
        println("Enter the amount of money you want to bet: ");
        printMoney(playerBalance);
        return readInt();
    }

    @Override
    public void showInvalidBetMessage() {
        println("Invalid amount of money, Try Again.");
    }

    @Override
    public void showBetWasMadeMessage(int bet){
        println("₱" + bet + " was put into the bet");
    }

    @Override
    public void displayRoundStart() {
        println("--------------");
        println("Round Started");
    }

    @Override
    public void showDealerIsShufflingTheDeckMessage() {
        println("The Dealer is shuffling the deck");
    }

    @Override
    public void showCardsDealtMessage() {
        println("The cards are being dealt");
    }

    @Override
    public void printAcquiredCardNotification(Card acquiredCard) {
        println("You got the card " + acquiredCard.getCardNotation() + "!");
    }

    @Override
    public void printDealersFaceUpCardNotification(Card faceUpCard) {
        println("The Dealer's face up card is " + faceUpCard.getCardNotation() + "!");
    }

    @Override
    public void printBlackjack() {
        println("BLACKJACK!BLACKJACK!BLACKJACK!");
    }

    @Override
    public void showDealerRevealingCardMessage() {
        println("The Dealer is revealing their face down card......");
    }

    @Override
    public void printDealersBlackjackPair(Card faceUpCard, Card faceDownCard) {
        println("The Dealers cards are: [" + faceUpCard.getCardNotation() + ", " + faceDownCard.getCardNotation() + "]!!");
    }

    @Override
    public void printPlayerCards(Player player, Dealer dealer) {
        println("--------------");
        println("Your cards: " + player.getDisplayedHand() + " = " + player.getHandTotalBlackJackValue());
        println("The Dealer's cards: " + dealer.getCards());
    }

    @Override
    public void displayPlayerOptions() {
        println("What do you do now?");
        println("1. Hit\n2. Stand");
    }

    @Override
    public void showDealerGivingCardMessage() {
        println("The Dealer gave you a card...");
    }

    @Override
    public void printRevealedCardNotification(Card hitCard) {
        println("It's a " + hitCard.getCardNotation() + "!");
    }

    @Override
    public void showDealerHitsMessage() {
        println("The Dealer has decided to hit for another card");
    }

    @Override
    public void printDealersTotalValue(int dealersTotalValue) {
        println("Dealer's Value: " + dealersTotalValue);
    }

    @Override
    public void printDealerBustsMessage() {
        println("Dealer Busts!");
    }

    @Override
    public void displayOutcomeMessage(Outcome outcome, int betMoney) {
        switch (outcome){
            case BLACKJACK:
                println("You won " + betMoney / 100 + "!!");
                break;
            case WIN:
                println("You win!");
                println("Winnings: " + betMoney / 100);
                break;
            case PUSH:
                println("Push");
                break;
            case LOSE:
                println("You lose!");
                break;
            case BUST:
                println("Bust!");
                println("You lose!");
                break;
            default: throw new IllegalArgumentException("Unknown Outcome");
        }
    }

}
