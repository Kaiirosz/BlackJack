package model;

import java.util.Objects;

public class Card {
    private final Value value;
    private final Suit suit;
    private final String icon;
    private final String cardName;

    public Card(Value value, Suit suit, String icon) {
        this.value = value;
        this.suit = suit;
        this.icon = icon;
        this.cardName = getValueName() + " of " + suit;
    }

    public String getValueName() {
        return value.name();
    }

    public Value getValue() {
        return value;
    }


    public String getCardName() {
        return cardName;
    }

    public int getBlackJackValue(int totalHandValue){
        return value.getBlackJackValue(totalHandValue);
    }

    public String getCardNotation() {
        if (value.ordinal() + 1 > 10 || value.ordinal() == 0) {
            String firstLetter = getValueName().split("")[0];
            return firstLetter + icon;
        }
        return value.ordinal() + 1 + icon;
    }


    @Override
    public String toString() {
        return getCardName() + ": " + getCardNotation();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Card card)) {
            return false;
        }
        return Objects.equals(this.value, card.value)
                && Objects.equals(this.suit, card.suit);
    }

}
