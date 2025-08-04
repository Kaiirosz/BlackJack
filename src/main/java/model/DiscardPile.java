package model;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private final List<Card> discardedCardsList;

    public DiscardPile(){
        this.discardedCardsList = new ArrayList<>();
    }

    public void add(Card card){
        discardedCardsList.add(card);
    }

    public List<Card> getAllCards(){
        return discardedCardsList;
    }
}
