package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck  {
    private final List<Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
        initializeDeck();
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public Card getFirstCard(){
        Card firstCard = cards.getFirst();
        cards.remove(firstCard);
        return firstCard;
    }


    public void shuffleDeck(){
        Collections.shuffle(cards);
    }

    public void initializeDeck(){
        Value[] values = Value.values();
        for (int i = 0; i <= 12; i++){
            addCard(new Card (values[i], Suit.HEARTS,"♥" ));
            addCard(new Card(values[i], Suit.DIAMONDS,"♦"));
            addCard(new Card(values[i], Suit.SPADES,"♠"));
            addCard(new Card(values[i], Suit.CLOVERS,"♣"));
        }
    }

    public List<Card> getAllCards(){
        return cards;
    }
}
