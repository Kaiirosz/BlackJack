package model;

import java.util.List;

public class Player {
    private final String name;
    private final Hand hand;

    public Player(String name) {
        this.name = name;
        hand = new Hand();
    }

    public String getName() {
        return name;
    }


    public void addToHand(Card card) {
        hand.addCard(card);
    }

    public int getHandSize() {
        return hand.getNumberOfCards();
    }

    public Card giveCardFromHand(int indexOfCard){
        return hand.giveCard(indexOfCard);
    }

    public String removePair(Card card, Card anotherCard) {
        hand.removePair(card, anotherCard);
        return "[" + card.getCardNotation() + ", " + anotherCard.getCardNotation() + "]";
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

    public void displayHiddenHand(){
        System.out.println(name + ": " + hand.displayHiddenHand());
    }

    public List<Card> getAllCards() {
        return hand.getAllCards();
    }

    @Override
    public String toString() {
        return name;
    }

}
