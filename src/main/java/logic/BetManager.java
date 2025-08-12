package logic;

import io.GameIO;
import model.Outcome;
import model.Player;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetManager {
    private final Map<Player, Integer> betsMap;

    public BetManager() {
        this.betsMap = new HashMap<>();
    }


    public void placePlayerBet(Player humanPlayer, int betAmount){
        humanPlayer.subtractMoney(betAmount);
        betsMap.put(humanPlayer, betAmount);
    }

    public int getPlayerBet(Player player){
        return betsMap.get(player);
    }

    public int placeAIBet(Player aiPlayer, int betAmount){
            int aiMoney = aiPlayer.getMoney();
            if (betAmount > aiMoney){
                aiPlayer.subtractMoney(aiMoney);
                betsMap.put(aiPlayer, aiMoney);
                return aiMoney;
            }
            else {
                aiPlayer.subtractMoney(betAmount);
                betsMap.put(aiPlayer, betAmount);
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
