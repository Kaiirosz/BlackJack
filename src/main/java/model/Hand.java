package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hand {
    private final List<Card> cards;
    private Outcome handOutcome;
    private int bet;
    private boolean isUnresolved;

    public Hand(){
        cards = new ArrayList<>();
        isUnresolved = true;
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

    public boolean isUnresolved() {
        return isUnresolved;
    }

    public void setUnresolved(boolean unresolved) {
        isUnresolved = unresolved;
    }

    public Card giveFirstCard(){
        Card firstCard = cards.getFirst();
        cards.remove(firstCard);
        return firstCard;
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

    public List<Card> getCards(){
        return cards;
    }

    public boolean isSplitPair(){
        return cards.size() == 2 &&
                cards.getFirst().getValueName().equals(cards.get(1).getValueName());
    }

    public boolean splitPairIsEightOrAces(){
        if (cards.size() > 2){
            return false;
        }
        String firstCardValueName = cards.getFirst().getValueName();
        String secondCardValueName = cards.get(1).getValueName();
        if (!firstCardValueName.equals(secondCardValueName)){
            return false;
        }
        return firstCardValueName.equals("ACE") || firstCardValueName.equals("EIGHT");
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
