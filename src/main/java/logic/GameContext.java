package logic;

import model.Dealer;
import model.Deck;

import java.util.ArrayList;

public class GameContext {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;

    public GameContext() {
        this.playerManager = new PlayerManager(new ArrayList<>());
        this.dealer = new Dealer(new Deck());
        this.betManager = new BetManager();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public BetManager getBetManager() {
        return betManager;
    }
}
