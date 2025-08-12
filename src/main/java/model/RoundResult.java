package model;

import java.util.Map;

public class RoundResult {
    private final Map<Player, Integer> playerWinningsMap;


    public RoundResult(Map<Player, Integer> playerWinningsMap) {
        this.playerWinningsMap = playerWinningsMap;
    }

}
