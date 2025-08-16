package logic;

import model.Hand;
import model.Outcome;
import model.Player;
import java.math.BigDecimal;

public class BetManager {

    public void placePlayerBet(Player humanPlayer, int betAmount){
        Hand hand = new Hand();
        int betAmountInCents = betAmount * 100;
        hand.setBet(betAmountInCents);
        humanPlayer.subtractMoney(betAmountInCents);
        humanPlayer.addHand(hand);
    }


    public int placeAIBet(Player aiPlayer, int betAmount){ //case where AI has no money to bet TBD
            Hand aiHand = new Hand();
            int aiMoney = aiPlayer.getMoney();
            int betAmountInCents = betAmount * 100;
            if (betAmountInCents > aiMoney){
                aiPlayer.subtractMoney(aiMoney);
                aiHand.setBet(aiMoney);
                aiPlayer.addHand(aiHand);
                return aiMoney / 100;
            }
            else {
                aiPlayer.subtractMoney(betAmountInCents);
                aiHand.setBet(betAmountInCents);
                aiPlayer.addHand(aiHand);
                return betAmount;
            }
    }

    public boolean isValidBet(Player player, int betInCents) {
        int playerBalance = player.getMoney();
        return betInCents <= playerBalance && betInCents > 0;
    }

    public int settleBetOutcome(Outcome outcome, int betMoney){
        BigDecimal bigDecimalBetMoney = BigDecimal.valueOf(betMoney);
        BigDecimal multiplier;
        switch (outcome){
            case BLACKJACK -> multiplier = BigDecimal.valueOf(2.5);
            case WIN -> multiplier = BigDecimal.valueOf(2);
            case PUSH -> multiplier = BigDecimal.valueOf(1);
            case LOSE, BUST -> multiplier = BigDecimal.valueOf(0);
            default -> throw new IllegalArgumentException("Unknown Outcome");
        }
        return bigDecimalBetMoney.multiply(multiplier).intValue();
    }

}
