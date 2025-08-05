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

    public void setName(String name){
        this.name = name;
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

    public void displayHand() {
        StringBuilder sb = new StringBuilder();
        List<Card> cardsInHand = hand.getAllCards();
        for (int i = 0; i < cardsInHand.size(); i++) {
            if (i == cardsInHand.size() - 1) {
                sb.append(cardsInHand.get(i).getCardNotation());
                continue;
            }
            sb.append(cardsInHand.get(i).getCardNotation()).append(", ");
        }
        System.out.println(sb);
    }


    public List<Card> getAllCards() {
        return hand.getAllCards();
    }

    @Override
    public String toString() {
        return name;
    }

}
