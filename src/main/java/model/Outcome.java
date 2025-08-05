package model;

public enum Outcome {
    WIN(2),
    BLACKJACK(2.5),
    LOSE(0),
    BUST(0);

    private final double multiplier;

    Outcome(double multiplier){
        this.multiplier = multiplier;
    }

    public double getMultiplier(){
        return multiplier;
    }
}
