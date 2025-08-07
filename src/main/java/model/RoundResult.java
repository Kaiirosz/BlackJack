package model;

public class RoundResult {
    private final Outcome outcome;
    private final int betResult;

    public RoundResult(Outcome outcome, int betResult) {
        this.outcome = outcome;
        this.betResult = betResult;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public int getBetResult(){
        return betResult;
    }
}
