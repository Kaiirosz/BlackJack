package handler;

import io.GameIO;
import logic.*;
import model.*;
import utils.GameUtils;

public class AITurnHandler implements TurnHandler{

    @Override
    public void handleTurn(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils) {
        PlayerManager playerManager = gameContext.getPlayerManager();
        AITurnLogic aiTurnLogic = new AITurnLogic(gameContext, roundOutcome);
            for (Player ai : playerManager.getAIPlayersInRound()) {
                io.showPlayerTurn(ai.getName());
                utils.pauseForEffect(1000);
                TurnResult turnResult = TurnResult.CONTINUE;
                while (turnResult.equals(TurnResult.CONTINUE)) {
                    Action action = aiTurnLogic.decideAction(ai);
                    switch (action) {
                        case HIT:
                            io.printAIHitsNotification(ai);
                            turnResult = aiTurnLogic.hit(ai);
                            Card cardHit = aiTurnLogic.getHitCard();
                            io.showDealerGivingCardMessage();
                            utils.pauseForEffect(1000);
                            io.printRevealedCardNotification(cardHit);
                            utils.pauseForEffect(1000);
                            io.printAICards(ai);
                            break;
                        case STAND:
                            io.printStandNotification();
                            turnResult = aiTurnLogic.stand(ai);
                            break;
                    }
                }
                if (turnResult.equals(TurnResult.BUST)) {
                    io.printPlayerBustNotification();
                    aiTurnLogic.removePlayerFromRound(ai);
                }
                utils.pauseForEffect(1000);
        }
    }
}
