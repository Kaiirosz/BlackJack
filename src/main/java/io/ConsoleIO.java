package io;

import logic.PlayerManager;
import model.*;

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
        return readLine();
    }

    @Override
    public int askForNumberOfAIPlayers() {
        println("How many other AI players do you want with you? (0-6)");
        return readInt();
    }

    @Override
    public void showInvalidNumberOfAIPlayersMessage(){
        println("Invalid amount of players. Try Again.");
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
    public void showAIBetMadeMessage(Player ai, int aiBet){
        println(ai.getName() + " has put ₱" + aiBet + " into the bet");
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
    public void printAcquiredCardNotification(Card firstCard, Card secondCard, Player player) {
        boolean isHuman = player.getIsHuman();
        if (isHuman) {
            println("You got the cards: [" + firstCard.getCardNotation() + ", " + secondCard.getCardNotation() + "]!");
        }
        else {
            println(player.getName() +  " got the cards: [" + firstCard.getCardNotation() + ", " + secondCard.getCardNotation() + "]!");
        }
    }

    @Override
    public void printDealersFaceUpCardNotification(Card faceUpCard) {
        println("The Dealer's face up card is " + faceUpCard.getCardNotation() + "!");
    }

    @Override
    public boolean askForInsurance(int halfBet){
        println("Do you want to pay for insurance (" + halfBet/100 + ") (Y/N)?");
        while (true) {
            String answer = readLine();
            if (answer.equalsIgnoreCase("Y")){
                return true;
            }
            else if (answer.equalsIgnoreCase("N")){
                return false;
            }
            else {
                println("Unknown input, enter 'Y' for yes and 'N' for no");
            }
        }
    }

    @Override
    public void printAIInsuresNotification(String aiName){
        println(aiName + " has decide to pay for insurance");
    }

    @Override
    public void printBlackjackNotification(Player p) {
        boolean isHuman = p.getIsHuman();
        if (isHuman){
            println("You hit Blackjack!!");
        }
        else {
            println(p.getName() +  " has hit Blackjack!!");
        }
    }

    @Override
    public void showDealerPeeksMessage(){
        println("The Dealer is peeking at their hole card...");
    }

    @Override
    public void showDealerRevealingCardMessage() {
        println("The Dealer is revealing their face down card......");
    }

    @Override
    public void printDealersBlackjackPair(Card faceUpCard, Card faceDownCard) {
        println("The Dealers cards are: [" + faceUpCard.getCardNotation() + ", " + faceDownCard.getCardNotation() + "]!");
        println("The Dealer also has Blackjack!");
    }

    @Override
    public void showInsuranceResolvedMessage(){
        println("Insurance Resolved");
    }

    @Override
    public void printAllCards(PlayerManager playerManager, Dealer dealer) {
        println("-------------------------------------------");
        if (playerManager.checkIfHumanIsInRound()){
            Player humanPlayer = playerManager.getHumanPlayer();
            println("Your cards: " + humanPlayer.getAllHandsAndValues());
        }
        for (Player p : playerManager.getAIPlayersInRound()){
            printAICards(p);
        }
        println("The Dealer's cards: " + dealer.getCards());
    }

    @Override
    public void showPlayerTurn(String name){
        println("\n" + name + "'s turn");
    }

    @Override
    public void displayPlayerOptions(boolean isFirstAction, boolean canAffordDoubleDown, boolean canSplit) {
        println("What do you do now?");
        StringBuilder sb = new StringBuilder("1. Hit\n2. Stand");
        if (isFirstAction){
            sb.append("\n3. Surrender");
        }
        if (isFirstAction && canAffordDoubleDown){
            sb.append("\n4. Double Down");
        }
        if (canSplit){
            sb.append("\n5. Split");
        }
        println(sb.toString());
    }


    @Override
    public void displayInvalidActionMessage(){
        println("Invalid Action. Try Again.");
    }

    @Override
    public void showDealerGivingCardMessage() {
        println("The Dealer gave a card...");
    }

    @Override
    public void printRevealedCardNotification(Card hitCard) {
        println("It's " + hitCard.getCardNotation() + "!");
    }

    @Override
    public void displayCardsSplitMessage(Card pairCard){
        println("The hand was split into two hands: [" + pairCard + ", ?] and [" + pairCard + ", ?]");
    }

    @Override
    public void printHittingForSplitHandNotification(){
        println("------------------------------------------");
        println("Now hitting for split hand");
    }

    @Override
    public void displayNextHandMessage(){
        println("Proceeding to next hand");
    }

    @Override
    public void printAIHitsNotification(Player ai){
        println(ai.getName() + " chose to hit!");
    }

    @Override
    public void printAIDoublesDownNotification(Player ai){
        println(ai.getName() + " has decided to double down!");
    }

    @Override
    public void printAISplitsNotification(Player ai){
        println(ai.getName() + " has decided to split!");
    }


    @Override
    public void printAICards(Player p){
        Hand initialAIHand = p.getFirstHand();
        println(p.getName() + "'s cards: " + p.getAllHandsAndValues());
    }

    @Override
    public void printStandNotification(){
        println("Stand");
    }

    @Override
    public void printPlayerBustNotification(){
        println("Bust!!");
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
    public void printRoundSummaryNotification(){
        println("\n" + "ROUND SUMMARY: \n");
    }

    @Override
    public void printPlayerRoundSummary(String summary){
        println(summary);
    }

    @Override
    public void showGameOverMessage(){
        println("Game Over");
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
