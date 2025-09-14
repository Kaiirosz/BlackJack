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

    public boolean isValidBet(int playerBalance, int bet) {
        int betInCents = bet * 100;
        return betInCents > 0 && betInCents <= playerBalance;
    }

    public void placeInsuranceBet(Player player, int insuranceBet) {
        player.subtractMoney(insuranceBet);
        insuredPlayersList.add(player);
    }

    public boolean aPlayerInsured(){
        return !insuredPlayersList.isEmpty();
    }

    public void settleInsuranceBets(boolean dealerHasBJ) {
        if (dealerHasBJ) {
            for (Player p : insuredPlayersList) {
                int halfBet = p.getInsuranceBet();
                int insuranceBetPayOut = halfBet * 2;
                p.addMoney(insuranceBetPayOut + halfBet);
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
