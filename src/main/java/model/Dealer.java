package model;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private final List<Card> cards;
    private final Deck deck;

    public Dealer(Deck deck){
        this.cards = new ArrayList<>();
        this.deck = deck;
    }
}
