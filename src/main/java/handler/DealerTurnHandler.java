package handler;

import io.GameIO;
import logic.AITurnLogic;
import logic.DealerTurnLogic;
import logic.GameContext;
import logic.PlayerManager;
import model.*;
import utils.GameUtils;

public class DealerTurnHandler implements TurnHandler {
    private final Dealer dealer;
    private final DealerTurnLogic dealerTurnLogic;
    private final RoundOutcome roundOutcome;
    private final GameIO io;
    private final GameUtils utils;

    public DealerTurnHandler(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils){
        this.dealer = gameContext.getDealer();
        this.roundOutcome = roundOutcome;
        this.dealerTurnLogic = new DealerTurnLogic(gameContext, this.roundOutcome);
        this.io = io;
        this.utils = utils;
    }

    @Override
    public void handleTurn() {
        io.showDealerRevealingCardMessage();
        utils.pauseForEffect(2000);
        io.printRevealedCardNotification(dealer.getFaceDownCard());
        dealerTurnLogic.settleDealerBJNoPlayerBJ();
        TurnResult turnResult = TurnResult.CONTINUE;
        while (turnResult.equals(TurnResult.CONTINUE)){
            Action action = dealerTurnLogic.decideAction();
            if (action.equals(Action.HIT)){
                turnResult = dealerTurnLogic.hit();
                utils.pauseForEffect(1000);
                io.showDealerHitsMessage();
                utils.pauseForEffect(2000);
                io.printRevealedCardNotification(dealer.getLastCard());
                int dealersTotalValue = dealer.getTotalBlackJackValue();
                io.printDealersTotalValue(dealersTotalValue);
            } else if (action.equals(Action.STAND)) {
                turnResult = dealerTurnLogic.stand();
                utils.pauseForEffect(1000);
                io.printStandNotification();
            }
        }
    }
}
