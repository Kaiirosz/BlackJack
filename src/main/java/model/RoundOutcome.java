package model;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class RoundOutcome {
    private Map<Player, PlayerRoundSummary> playerRoundSummaryMap;

    public RoundOutcome(){
        this.playerRoundSummaryMap = new HashMap<>();
    }


    public void addPlayerOutcome(Player player, int totalBetResult){
        playerRoundSummaryMap.put(player, new PlayerRoundSummary(player, totalBetResult));
    }
//
//    public PlayerRoundSummary getPlayerRoundSummary(Player player){
//        return playerRoundSummaryMap.get(player);
//    }
//
//    public Map<Player, PlayerRoundSummary> getPlayerRoundSummaryMap() {
//        return playerRoundSummaryMap;
//    }
}
