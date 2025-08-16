package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final String name;
    private int money;
    private final List<Hand> handList;
    private final boolean isHuman;

    public Player(String name, int money, boolean isHuman) {
        this.name = name;
        this.money = money;
        this.isHuman = isHuman;
        handList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getMoney(){
        return money;
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
        money += addend;
    }

    public void subtractMoney(int subtrahend){
        money-= subtrahend;
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return money == player.money && isHuman == player.isHuman && Objects.equals(name, player.name) && Objects.equals(hand, player.hand);
    }

}
