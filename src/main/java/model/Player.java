package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final String name;
    private int balance;
    private final List<Hand> handList;
    private final boolean isHuman;

    public Player(String name, int balance, boolean isHuman) {
        this.name = name;
        this.balance = balance;
        this.isHuman = isHuman;
        handList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getBalance(){
        return balance;
    }

    public boolean getIsHuman() {
        return isHuman;
    }

    public List<Hand> getHandList(){
        return handList;
    }

    public void addHand(Hand hand){
        handList.add(hand);
    }

    public int getHandBet(Hand handToGetBetFrom){
        return handToGetBetFrom.getBet();
    }

    public void addMoney(int addend){
        balance += addend;
    }

    public void subtractMoney(int subtrahend){
        balance -= subtrahend;
    }


    public void addCardToHand(Card card, Hand handToAddCard) {
        handToAddCard.addCard(card);
    }

    public Hand getFirstHand(){
        return handList.getFirst();
    }

    public String getDisplayedHand(Hand handToDisplay) {
        return handToDisplay.getDisplayedHand();
    }

    public int getHandTotalBlackJackValue(Hand hand){
        return hand.getTotalBlackJackValue();
    }

    public String getAllHandsAndOutcomes(){
        StringBuilder handsAndOutcomes = new StringBuilder();
        for (int i = 0; i < handList.size(); i++){
            String handAndOutcome = handList.get(i).getHandAndOutcome();
            handsAndOutcomes.append(handAndOutcome);
            if (i < handList.size() - 1){
                handsAndOutcomes.append("\n");
            }
        }
        return handsAndOutcomes.toString();
    }

    public boolean canAffordDoubleDown(){
        int doubleDownAmount = getHandBet(getFirstHand());
        return balance >= doubleDownAmount;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(name, player.name) &&
                isHuman == player.isHuman;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isHuman); //must be consistent with equals
    }
}
