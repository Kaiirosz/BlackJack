package logic;

import io.GameIO;
import model.Outcome;
import model.Player;

import java.math.BigDecimal;

public class BetManager {
    private final GameIO io;

    public BetManager(GameIO io) {
        this.io = io;
    }

    public int askAndPlaceBet(Player player) {
        while (true) {
            int bet = io.askForBetAmount(player.getMoney());
            int betInCents = bet * 100;
            if (isValidBet(player, betInCents)) {
                player.subtractMoney(betInCents);
                io.showBetWasMadeMessage(bet);
                return betInCents;
            }
            io.showInvalidBetMessage();
        }
    }

    private boolean isValidBet(Player player, int betInCents) {
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
