package model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand(){
        cards = new ArrayList<>();
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void removePair(Card card, Card anotherCard){
        if (!cards.contains(card) || !cards.contains(anotherCard)){
            throw new IllegalArgumentException("Incorrect cards!!");
        }
        cards.remove(card);
        cards.remove(anotherCard);
    }

    public Card giveCard(int index){
        Card givenCard = cards.get(index);
        cards.remove(givenCard);
        return givenCard;
    }

    public String displayHiddenHand(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append("\uD83C\uDCA0").append(" ");
        }
        return sb.toString();
    }

    public int getNumberOfCards(){
        return cards.size();
    }

    public List<Card> getAllCards(){
        return cards;
    }


}
