package model;

import java.util.HashMap;
import java.util.Map;

public class RoundOutcome {
    private final Map<Player, PlayerRoundSummary> playerRoundSummaryMap;

    public RoundOutcome(){
        this.playerRoundSummaryMap = new HashMap<>();
    }

    public void addPlayerOutcome(Player player, int totalBetResult){
        playerRoundSummaryMap.put(player, new PlayerRoundSummary(player, totalBetResult));
    }

    public PlayerRoundSummary getPlayerRoundSummary(Player player){
        if (!playerRoundSummaryMap.containsKey(player)){
            throw new IllegalArgumentException("Equals Wrong. Player not found");
        }
        return playerRoundSummaryMap.get(player);
    }

    public int getPlayerTotalBetResult(Player player){
        if (!playerRoundSummaryMap.containsKey(player)){
            throw new IllegalArgumentException("Equals Wrong. Player not found");
        }
        return playerRoundSummaryMap.get(player).totalBetResult();
    }

    public Map<Player, PlayerRoundSummary> getPlayerRoundSummaryMap() {
        return playerRoundSummaryMap;
    }

    public void clear(){
        playerRoundSummaryMap.clear();
    }
}
