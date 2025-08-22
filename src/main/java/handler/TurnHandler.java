package handler;

import io.GameIO;
import logic.BetManager;
import logic.GameContext;
import logic.PlayerManager;
import model.Dealer;
import model.Player;
import model.RoundOutcome;
import utils.GameUtils;

public interface TurnHandler {
     void handleTurn(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils);
}
