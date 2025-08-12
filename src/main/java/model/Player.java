package model;

import logic.TurnStrategy;

import java.util.List;
import java.util.Objects;

public class Player {
    private final String name;
    private int money;
    private final Hand hand;
    private final boolean isHuman;
    private final TurnStrategy turnStrategy;

    public Player(String name, int money, boolean isHuman, TurnStrategy turnStrategy) {
        this.name = name;
        this.money = money;
        this.isHuman = isHuman;
        this.turnStrategy = turnStrategy;
        hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public int getMoney(){
        return money;
    }

    public boolean getIsHuman(){
        return isHuman;
    }

    public TurnStrategy getTurnStrategy() {
        return turnStrategy;
    }

    public void addMoney(int addend){
        money += addend;
    }

    public void subtractMoney(int subtrahend){
        money-= subtrahend;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return money == player.money && isHuman == player.isHuman && Objects.equals(name, player.name) && Objects.equals(hand, player.hand);
    }

}
