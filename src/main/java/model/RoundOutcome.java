package model;

import java.util.Map;

public class RoundOutcome {
    private Map<Player, Outcome> playerOutcomeMap;

    public RoundOutcome(Map<Player, Outcome> playerOutcomeMap){
        this.playerOutcomeMap = playerOutcomeMap;
    }

    public void addPlayerOutcome(Player player, Outcome outcome){
        playerOutcomeMap.put(player, outcome);
    }

    public Map<Player, Outcome> getPlayerOutcomeMap() {
        return playerOutcomeMap;
    }
}
