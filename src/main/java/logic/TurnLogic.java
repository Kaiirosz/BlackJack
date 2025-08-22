package logic;

import model.Player;
import model.TurnResult;

public interface TurnLogic {
    TurnResult hit(Player player);
    TurnResult stand(Player player);
}
