package handler;

import io.GameIO;
import logic.*;
import model.*;
import utils.GameUtils;

public class AITurnHandler implements TurnHandler{
    private final GameContext gameContext;
    private final PlayerManager playerManager;
    private final RoundOutcome roundOutcome;
    private final GameIO io;
    private final GameUtils utils;

    public AITurnHandler(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils){
        this.gameContext = gameContext;
        this.playerManager = gameContext.getPlayerManager();
        this.roundOutcome = roundOutcome;
        this.io = io;
        this.utils = utils;
    }

    @Override
    public void handleTurn() {
            for (Player ai : playerManager.getAIPlayersInRound()) {
                Hand currentHand = ai.getFirstUnresolvedHand();
                AITurnLogic aiTurnLogic = new AITurnLogic(gameContext, roundOutcome, ai, currentHand);
                io.showPlayerTurn(ai.getName());
                utils.pauseForEffect(1000);
                boolean isFirstAction = true;
                TurnResult turnResult = TurnResult.CONTINUE;
                while (turnResult.equals(TurnResult.CONTINUE)) {
                    Action action = aiTurnLogic.decideAction(isFirstAction);
                    switch (action) {
                        case HIT:
                            io.printAIHitsNotification(ai);
                            turnResult = aiTurnLogic.hit();
                            Card cardHit = aiTurnLogic.getHitCard();
                            io.showDealerGivingCardMessage();
                            utils.pauseForEffect(1000);
                            io.printRevealedCardNotification(cardHit);
                            utils.pauseForEffect(1000);
                            io.printAICards(ai);
                            break;
                        case STAND:
                            turnResult = aiTurnLogic.stand();
                            break;
                        case DOUBLE_DOWN:
                            io.printAIDoublesDownNotification(ai);
                            turnResult = aiTurnLogic.doubleDown();
                            Card cardHit2 = aiTurnLogic.getHitCard();
                            io.showDealerGivingCardMessage();
                            utils.pauseForEffect(1000);
                            io.printRevealedCardNotification(cardHit2);
                            utils.pauseForEffect(1000);
                            io.printAICards(ai);
                            break;
                    }
                    isFirstAction = false;
                }
                if (turnResult.equals(TurnResult.STAND)){
                    io.printStandNotification();
                }
                if (turnResult.equals(TurnResult.BUST)) {
                    io.printPlayerBustNotification();
                    aiTurnLogic.removePlayerFromRound();
                }
                utils.pauseForEffect(1000);
        }
    }
}
