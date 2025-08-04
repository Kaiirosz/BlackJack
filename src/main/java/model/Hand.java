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

    public void displayHand() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i == cards.size() - 1) {
                sb.append(cards.get(i).getCardNotation());
                continue;
            }
            sb.append(cards.get(i).getCardNotation()).append(", ");
        }
        System.out.println(sb);
    }



    public int getNumberOfCards(){
        return cards.size();
    }

    public List<Card> getAllCards(){
        return cards;
    }


}
