package model;

public enum Value {
    ACE {
        @Override
        public int getBlackJackValue(int totalHandValue) {
            return totalHandValue > 10 ? 1 : 11;
        }
    },
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    private final int fixedValue;

    Value(int value) {
        this.fixedValue = value;
    }

    Value() {
        this.fixedValue = -1;
    }

    public int getBlackJackValue(int totalHandValue) {
        return fixedValue;
    }
}

