package logic;

import model.HandStrength;
import model.Player;

import java.util.Random;

public class AIInsuranceLogic { ;
    private final Random random = new Random();

    public boolean decideToInsure(Player ai){
        int totalValue = ai.getHandTotalBlackJackValue(ai.getFirstHand());
        HandStrength aiHandStrength = HandStrength.ofPlayerValue(totalValue);
        if (aiHandStrength == HandStrength.STRONG) {
            return random.nextInt(100) + 1 <= 30;
        }
        return false;
    }
}
