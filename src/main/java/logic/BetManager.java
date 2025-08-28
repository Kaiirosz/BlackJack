package logic;

import model.Hand;
import model.Outcome;
import model.Player;
import java.math.BigDecimal;

public class BetManager {

    public void placePlayerBet(Player player, int betAmountInCents, Hand handToBetIn){
        handToBetIn.setBet(betAmountInCents);
        player.subtractMoney(betAmountInCents);
    }

    public void doubleDownBet(Player player, Hand handToDoubleDown){
        int betInCents = handToDoubleDown.getBet();
        handToDoubleDown.setBet(betInCents * 2);
        player.subtractMoney(betInCents);
    }

    public int placeAIBet(Player aiPlayer, int betAmount, Hand handToBetIn){ //case where AI has no money to bet TBD
            int aiMoney = aiPlayer.getBalance();
            if (betAmount > aiMoney){
                aiPlayer.subtractMoney(aiMoney);
                handToBetIn.setBet(aiMoney);
                return aiMoney / 100;
            }
            else {
                aiPlayer.subtractMoney(betAmount);
                handToBetIn.setBet(betAmount);
                return betAmount / 100;
            }
    }

    public boolean isValidBet(int playerBalance, int bet) {
        int betInCents = bet * 100;
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
