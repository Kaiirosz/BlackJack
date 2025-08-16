package model;

import java.util.HashMap;
import java.util.Map;

public class PlayerRoundSummary {
    private final Player player;
    private final int totalBetResult;

    public PlayerRoundSummary(Player player, int totalBetResult){
        this.player = player;
        this.totalBetResult = totalBetResult;
    }

    public String getSummary(){
        return player.getName() + "\n" +
                player.getAllHandsAndOutcomes() + "\n" +
                "Total Bet Result: " + totalBetResult;
    }
}

