package model;

import logic.PlayerManager;

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

    public void dealCards(PlayerManager playerManager){
/*        Card pairCard = new Card(Value.ACE, Suit.SPADES, "♠");
        Card pairCard2 = new Card(Value.ACE, Suit.SPADES, "♥");*/
        for (Player player : playerManager.getAllPlayersInRound()){
/*            if (player.getIsHuman()){
                Hand AIInitialHand = player.getFirstHand();
                player.addCardToHand(pairCard, AIInitialHand);
                player.addCardToHand(pairCard2, AIInitialHand);
                continue;
            }*/
            Hand initialHand = player.getFirstHand();
            player.addCardToHand(deck.getFirstCard(), initialHand);
            player.addCardToHand(deck.getFirstCard(), initialHand);
        }
/*        cards.add(pairCard);
        cards.add(pairCard2);*/
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

    public void returnCardsToDeck(PlayerManager playerManager){
        for (Player p : playerManager.getAllPlayers()){
            List<Hand> playerHands = p.getHandList();
            for (Hand hand : playerHands){
                List<Card> playerCards = hand.getCards();
//                hand.setHandOutcome(null);
                deck.getAllCards().addAll(playerCards);
                playerCards.clear();
            }
            playerHands.clear();
        }
        deck.getAllCards().addAll(cards);
        cards.clear();
    }

    public boolean hasFaceUpAce(){
        return getFaceUpCard().getValue() == Value.ACE;
    }

}
