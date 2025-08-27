package logic;

import model.Player;
import model.TurnResult;

public interface TurnLogic {
    TurnResult hit();
    TurnResult stand();
    TurnResult doubleDown();
    TurnResult split();
}
