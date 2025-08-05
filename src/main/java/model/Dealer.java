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

    public void shuffleDeck(){
        deck.shuffleDeck();
    }

    public void dealCards(Player player){
        player.addCardToHand(deck.getFirstCard());
        player.addCardToHand(deck.getFirstCard());
        cards.add(deck.getFirstCard());
        cards.add(deck.getFirstCard());
    }

    public String getCards(){
        return "[" + getFaceUpCardNotation() + ", ?]";
    }

    public String getFaceUpCardNotation(){
        return cards.getFirst().getCardNotation();
    }
}
