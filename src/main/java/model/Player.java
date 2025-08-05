package model;

import java.util.List;

public class Player {
    private String name;
    private int money;
    private final Hand hand;

    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public int getMoney(){
        return money;
    }

    public void setMoney(int newMoney){
        money = newMoney;
    }


    public void addCardToHand(Card card) {
        hand.addCard(card);
    }

    public int getHandSize() {
        return hand.getNumberOfCards();
    }

    public String getDisplayedHand() {
        return hand.getDisplayedHand();
    }

    public int getHandTotalBlackJackValue(){
        return hand.getTotalBlackJackValue();
    }


    public List<Card> getAllCards() {
        return hand.getAllCards();
    }

    @Override
    public String toString() {
        return name;
    }

}
