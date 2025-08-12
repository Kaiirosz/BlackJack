package logic;

import io.GameIO;
import model.Dealer;
import utils.GameUtils;

public class GameContext {
    private TurnStrategy currentTurnStrategy;
    private final PlayerManager playerManager;
    private final Dealer dealer;

    public GameContext(TurnStrategy currentTurnStrategy, PlayerManager playerManager, Dealer dealer) {
        this.currentTurnStrategy = currentTurnStrategy;
        this.playerManager = playerManager;
        this.dealer = dealer;
    }

    public void setCurrentTurnStrategy(TurnStrategy turnStrategy){
        this.currentTurnStrategy = turnStrategy;
    }

    public void executeTurnStrategy(){

    }
}
