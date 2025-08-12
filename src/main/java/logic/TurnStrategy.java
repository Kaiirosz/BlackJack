package logic;

import io.GameIO;
import model.Dealer;
import utils.GameUtils;

public interface TurnStrategy {
    void executeTurn(PlayerManager playerManager, Dealer dealer, GameIO io, GameUtils utils);
}
