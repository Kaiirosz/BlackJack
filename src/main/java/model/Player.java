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

    public Hand addNewHand(){
        Hand hand = new Hand();
        handList.add(hand);
        return hand;
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
        if (!handList.contains(handToAddCard)){
            throw new IllegalArgumentException("Hand given is not valid hand of player");
        }
        handToAddCard.addCard(card);
    }

    public Hand getFirstHand(){
        return handList.getFirst();
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

    public String getAllHandsAndValues(){
        StringBuilder sb = new StringBuilder();
        for (Hand h : handList){
            if (h.equals(getFirstUnresolvedHand()) && isHuman){
                sb.append("Current: ").append(h.getDisplayedHand()).append(" = ").append(h.getTotalBlackJackValue()).append(", ");
                continue;
            }
            sb.append(h.getDisplayedHand()).append(" = ").append(h.getTotalBlackJackValue()).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    public boolean canAffordDoubleDown(Hand handToDoubleDown){
        int doubleDownAmount = getHandBet(handToDoubleDown);
        return balance >= doubleDownAmount;
    }

    public boolean canSplit(Hand handToSplit){
        if (!canAffordDoubleDown(handToSplit)){
            return false;
        }
        return handToSplit.isSplitPair();
    }

    public boolean hasSplit(){
        return handList.size() > 1;
    }


    public boolean hasUnresolvedHand(){
        for (Hand h : handList){
            if (h.isUnresolved()){
                return true;
            }
        }
        return false;
    }

    public Hand getFirstUnresolvedHand(){
        for (Hand h : handList){
            if (h.isUnresolved()){
                return h;
            }
        }
        throw new RuntimeException("Bad Method Call. No unresolved hand left.");
    }

    public boolean allHandsHaveOutcomes(){
        for (Hand hand : handList){
            if (hand.getHandOutcome() == null){
                return false;
            }
        }
        return true;
    }

    public Hand splitHand(Hand handToSplit){
        Hand splitHand = addNewHand();
        splitHand.addCard(handToSplit.giveFirstCard());
        return splitHand;
    }

    public boolean canAffordInsurance(){
        if (handList.isEmpty()){
            throw new RuntimeException("This player does not have any hands");
        }
        return balance >= getFirstHand().getBet() / 2;
    }

    public int getInsuranceBet(){
        return getFirstHand().getBet() / 2;
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
