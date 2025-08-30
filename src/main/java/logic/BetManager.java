package logic;

import model.Hand;
import model.Outcome;
import model.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BetManager {
    private final List<Player> insuredPlayersList;

    public BetManager() {
        insuredPlayersList = new ArrayList<>();
    }

    public void placePlayerBet(Player player, int betAmountInCents, Hand handToBetIn) {
        handToBetIn.setBet(betAmountInCents);
        player.subtractMoney(betAmountInCents);
    }

    public void doubleDownBet(Player player, Hand handToDoubleDown) {
        int betInCents = handToDoubleDown.getBet();
        handToDoubleDown.setBet(betInCents * 2);
        player.subtractMoney(betInCents);
    }

    public int placeAIBet(Player aiPlayer, int betAmount, Hand handToBetIn) { //case where AI has no money to bet TBD
        int aiMoney = aiPlayer.getBalance();
        if (betAmount > aiMoney) {
            aiPlayer.subtractMoney(aiMoney);
            handToBetIn.setBet(aiMoney);
            return aiMoney / 100;
        } else {
            aiPlayer.subtractMoney(betAmount);
            handToBetIn.setBet(betAmount);
            return betAmount / 100;
        }
    }

    public boolean isValidBet(int playerBalance, int bet) {
        int betInCents = bet * 100;
        return betInCents <= playerBalance && betInCents > 0;
    }

    public void placeInsuranceBet(Player player, int insuranceBet) {
        player.subtractMoney(insuranceBet);
        insuredPlayersList.add(player);
    }

    public boolean aPlayerInsured(){
        return !insuredPlayersList.isEmpty();
    }

    public void settleInsuranceBets(boolean dealerHasBJ) {
        int insuranceBet;
        if (dealerHasBJ) {
            for (Player p : insuredPlayersList) {
                int halfBet = p.getHalfOriginalBet();
                insuranceBet = halfBet * 2;
                p.addMoney(insuranceBet + halfBet);
            }
        }
    }

    public void clearInsuredPlayersList(){
        insuredPlayersList.clear();
    }

    public int settleBetOutcome(Outcome outcome, int betMoney) {
        BigDecimal bigDecimalBetMoney = BigDecimal.valueOf(betMoney);
        BigDecimal multiplier;
        switch (outcome) {
            case BLACKJACK -> multiplier = BigDecimal.valueOf(2.5);
            case WIN -> multiplier = BigDecimal.valueOf(2);
            case PUSH -> multiplier = BigDecimal.valueOf(1);
            case LOSE, BUST -> multiplier = BigDecimal.valueOf(0);
            case SURRENDER -> multiplier = BigDecimal.valueOf(0.5);
            default -> throw new IllegalArgumentException("Unknown Outcome");
        }
        return bigDecimalBetMoney.multiply(multiplier).intValue();
    }

}
