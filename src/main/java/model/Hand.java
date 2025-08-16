package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hand {
    private final List<Card> cards;
    private Outcome handOutcome;
    private int bet;

    public Hand(){
        cards = new ArrayList<>();
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public Card getCard(int index){
        if (index > cards.size() - 1 || index < 0){
            throw new IndexOutOfBoundsException("No such index!");
        }
        return cards.get(index);
    }

    public Outcome getHandOutcome(){
        return handOutcome;
    }

    public void setHandOutcome(Outcome handOutcome){
        this.handOutcome = handOutcome;
    }

    public String getDisplayedHand() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            if (i == cards.size() - 1) {
                sb.append(cards.get(i).getCardNotation()).append("]");
                continue;
            }
            sb.append(cards.get(i).getCardNotation()).append(", ");
        }
        return sb.toString();
    }

    public String getHandAndOutcome(){
        return cards + " = " + handOutcome.toString();
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

    public void setBet(int bet){
        this.bet = bet;
    }

    public int getBet(){
        return bet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hand hand)) return false;
        return Objects.equals(cards, hand.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cards);
    }
}
