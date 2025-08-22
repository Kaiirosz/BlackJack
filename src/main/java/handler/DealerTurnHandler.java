package handler;

import io.GameIO;
import logic.DealerTurnLogic;
import logic.GameContext;
import logic.PlayerManager;
import model.*;
import utils.GameUtils;

public class DealerTurnHandler implements TurnHandler {

    @Override
    public void handleTurn(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils) {
        Dealer dealer = gameContext.getDealer();
        DealerTurnLogic dealerTurnLogic = new DealerTurnLogic(gameContext, roundOutcome);
        io.showDealerRevealingCardMessage();
        utils.pauseForEffect(2000);
        io.printRevealedCardNotification(dealer.getFaceDownCard());
        dealerTurnLogic.settleDealerBJNoPlayerBJ();
        TurnResult turnResult = TurnResult.CONTINUE;
        while (turnResult.equals(TurnResult.CONTINUE)){
            Action action = dealerTurnLogic.decideAction();
            if (action.equals(Action.HIT)){
                turnResult = dealerTurnLogic.hit(new Player("Dealer", 21, false));
                utils.pauseForEffect(1000);
                io.showDealerHitsMessage();
                utils.pauseForEffect(2000);
                io.printRevealedCardNotification(dealer.getLastCard());
                int dealersTotalValue = dealer.getTotalBlackJackValue();
                io.printDealersTotalValue(dealersTotalValue);
            } else if (action.equals(Action.STAND)) {
                turnResult = dealerTurnLogic.stand(new Player("Dealer", 21, false));
                utils.pauseForEffect(1000);
                io.printStandNotification();
            }
        }
    }
}
