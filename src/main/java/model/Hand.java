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



    public int getNumberOfCards(){
        return cards.size();
    }

    public List<Card> getAllCards(){
        return cards;
    }


}
