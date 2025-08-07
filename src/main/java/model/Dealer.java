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

    public Card giveCard(){
        return deck.getFirstCard();
    }

    public String getCards(){
        return "[" + getFaceUpCard().getCardNotation() + ", ?]";
    }

    public Card getFaceUpCard(){
        return cards.getFirst();
    }

    public Card getFaceDownCard(){
        return cards.get(1);
    }

    public Card getLastCard(){
        return cards.getLast();
    }

    public int getTotalBlackJackValue(){
        int totalValue = 0;
        List<Card> acesList= new ArrayList<>();
        for (Card c : cards){
            if (c.getValue().equals(Value.ACE)){
                acesList.add(c);
                continue;
            }
            totalValue += c.getBlackJackValue(totalValue);
        }
        for (Card ace: acesList){
            totalValue += ace.getBlackJackValue(totalValue);
        }
        return totalValue;
    }

    public void hit(){
        cards.add(deck.getFirstCard());
    }

    public void returnCardsToDeck(List<Card> playerCards){
        deck.getAllCards().addAll(playerCards);
        deck.getAllCards().addAll(cards);
        playerCards.clear();
        cards.clear();
    }

}
