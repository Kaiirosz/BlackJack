package handler;

import io.GameIO;
import logic.DealerTurnLogic;
import logic.GameContext;
import model.*;
import utils.GameUtils;

public class DealerTurnHandler implements TurnHandler {
    private final Dealer dealer;
    private final DealerTurnLogic dealerTurnLogic;
    private final GameIO io;
    private final GameUtils utils;

    public DealerTurnHandler(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils){
        this.dealer = gameContext.getDealer();
        this.dealerTurnLogic = new DealerTurnLogic(gameContext, roundOutcome);
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
                presentDealerHit();
            } else if (action.equals(Action.STAND)) {
                turnResult = dealerTurnLogic.stand();
                presentDealerStand();
            }
        }
    }

    private void presentDealerHit(){
        utils.pauseForEffect(1000);
        io.showDealerHitsMessage();
        utils.pauseForEffect(2000);
        io.printRevealedCardNotification(dealer.getLastCard());
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        io.printDealersTotalValue(dealersTotalValue);
    }

    private void presentDealerStand(){
        utils.pauseForEffect(1000);
        io.printStandNotification();
    }

}
