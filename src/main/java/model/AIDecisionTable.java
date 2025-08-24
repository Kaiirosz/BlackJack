package model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AIDecisionTable {
    private final List<HandMatchup> handMatchUps;
    private final HandStrength aiHandStrength;
    private final HandStrength dealerHandStrength;
    private final boolean isFirstAction;
    private final boolean canAffordDoubleDown;

    public AIDecisionTable(HandStrength aiHandStrength, HandStrength dealerHandStrength, boolean isFirstAction, boolean canAffordDoubleDown) {
        this.aiHandStrength = aiHandStrength;
        this.dealerHandStrength = dealerHandStrength;
        this.isFirstAction = isFirstAction;
        this.canAffordDoubleDown = canAffordDoubleDown;
        handMatchUps = new ArrayList<>();
        initializeDecisionTable();
    }

    private void initializeDecisionTable() {
        handMatchUps.add(new HandMatchup(HandStrength.STRONG, HandStrength.STRONG)
                .withAction(Action.STAND, 100));
        handMatchUps.add(new HandMatchup(HandStrength.HIGH, HandStrength.STRONG)
                .withAction(Action.HIT, 100));
        handMatchUps.add(new HandMatchup(HandStrength.MEDIUM, HandStrength.STRONG)
                .withAction(Action.HIT, 100));
        handMatchUps.add(new HandMatchup(HandStrength.WEAK, HandStrength.STRONG)
                .withAction(Action.HIT, 100));
        handMatchUps.add(new HandMatchup(HandStrength.STRONG, HandStrength.MEDIUM)
                .withAction(Action.STAND, 100));
        handMatchUps.add(new HandMatchup(HandStrength.HIGH, HandStrength.MEDIUM)
                .withAction(Action.STAND, 70)
                .withAction(Action.HIT, 30));
        handMatchUps.add(new HandMatchup(HandStrength.WEAK, HandStrength.MEDIUM)
                .withAction(Action.HIT, 100));
        handMatchUps.add(new HandMatchup(HandStrength.STRONG, HandStrength.WEAK)
                .withAction(Action.STAND, 100));
        handMatchUps.add(new HandMatchup(HandStrength.HIGH, HandStrength.WEAK)
                .withAction(Action.STAND, 100));
        handMatchUps.add(new HandMatchup(HandStrength.WEAK, HandStrength.WEAK)
                .withAction(Action.HIT, 100));

        if (isFirstAction && canAffordDoubleDown){
            handMatchUps.add(new HandMatchup(HandStrength.MEDIUM, HandStrength.MEDIUM)
                    .withAction(Action.DOUBLE_DOWN, 80)
                    .withAction(Action.HIT, 20));
            handMatchUps.add(new HandMatchup(HandStrength.MEDIUM, HandStrength.WEAK)
                    .withAction(Action.DOUBLE_DOWN, 100));
        }
        else {
            handMatchUps.add(new HandMatchup(HandStrength.MEDIUM, HandStrength.MEDIUM)
                    .withAction(Action.HIT, 100));
            handMatchUps.add(new HandMatchup(HandStrength.MEDIUM, HandStrength.WEAK)
                    .withAction(Action.HIT, 100));
        }
    }

    public Action getAction(){
        HandMatchup currentHandMatchup = new HandMatchup(aiHandStrength, dealerHandStrength);
        for (HandMatchup matchUp : handMatchUps){
            if (matchUp.equals(currentHandMatchup)){
                return matchUp.getAction();
            }
        }
        throw new RuntimeException("Match up not found!");
    }
}
