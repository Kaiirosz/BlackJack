package handler;

import io.GameIO;
import logic.BetManager;
import logic.GameContext;
import logic.PlayerManager;
import logic.PlayerTurnLogic;
import model.Dealer;
import model.Outcome;
import model.Player;
import model.RoundOutcome;
import utils.GameUtils;

import java.util.Optional;

public class PlayerTurnHandler implements TurnHandler {
    @Override
    public void handleTurn(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils) {
        PlayerTurnLogic playerTurnLogic = new PlayerTurnLogic(gameContext, roundOutcome);
        while (true) {
            io.displayPlayerOptions();
            String action = io.readLine();
            if (action.equalsIgnoreCase("Hit")) {
                playerTurnLogic.hit();
            } else if (action.equalsIgnoreCase("Stand")) {
                return;
            } else {
                io.displayUnknownActionMessage();
            }
        }
    }

    private Optional<>

}
