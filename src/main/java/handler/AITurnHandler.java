package handler;

import io.GameIO;
import logic.*;
import model.*;
import utils.GameUtils;

public class AITurnHandler implements TurnHandler {
    private final GameContext gameContext;
    private final PlayerManager playerManager;
    private final RoundOutcome roundOutcome;
    private final GameIO io;
    private final GameUtils utils;

    public AITurnHandler(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils) {
        this.gameContext = gameContext;
        this.playerManager = gameContext.getPlayerManager();
        this.roundOutcome = roundOutcome;
        this.io = io;
        this.utils = utils;
    }

    @Override
    public void handleTurn() {
        for (Player ai : playerManager.getAIPlayersInRound()) {
            io.showPlayerTurn(ai.getName());
            utils.pauseForEffect(1000);
            while (ai.hasUnresolvedHand()) {
                Hand currentHand = ai.getFirstUnresolvedHand();
                AITurnLogic aiTurnLogic = new AITurnLogic(gameContext, roundOutcome, ai, currentHand);
                processAction(ai, aiTurnLogic, currentHand);
            }
            utils.pauseForEffect(1000);
        }
    }

    private void processAction(Player ai, AITurnLogic aiTurnLogic, Hand currentHand) {
        boolean isFirstAction = true;
        TurnResult turnResult = TurnResult.CONTINUE;
        Card cardHit;
        while (turnResult.equals(TurnResult.CONTINUE)) {
            Action action = aiTurnLogic.decideAction(isFirstAction);
            switch (action) {
                case HIT:
                    io.printAIHitsNotification(ai);
                    turnResult = aiTurnLogic.hit();
                    cardHit = aiTurnLogic.getLastCardHit();
                    revealDealtCard(cardHit);
                    io.printAICards(ai);
                    isFirstAction = false;
                    break;
                case STAND:
                    turnResult = aiTurnLogic.stand();
                    break;
                case DOUBLE_DOWN:
                    io.printAIDoublesDownNotification(ai);
                    turnResult = aiTurnLogic.doubleDown();
                    cardHit = aiTurnLogic.getLastCardHit();
                    revealDealtCard(cardHit);
                    io.printAICards(ai);
                    isFirstAction = false;
                    break;
                case SPLIT:
                    io.printAISplitsNotification(ai);
                    utils.pauseForEffect(1000);
                    turnResult = aiTurnLogic.split();
                    io.displayCardsSplitMessage(currentHand.getCard(0));
                    utils.pauseForEffect(1000);
                    Card cardHitForFirstHand = aiTurnLogic.getSecondToLastCardHit();
                    Card cardHitForSecondHand = aiTurnLogic.getLastCardHit();
                    revealDealtCard(cardHitForFirstHand);
                    io.printHittingForSplitHandNotification();
                    revealDealtCard(cardHitForSecondHand);
                    io.printAICards(ai);
                    break;
            }
        }
        if (turnResult.equals(TurnResult.STAND)) {
            io.printStandNotification();
        }
        if (turnResult.equals(TurnResult.BUST)) {
            io.printPlayerBustNotification();
            aiTurnLogic.resolveBust();
        }
        aiTurnLogic.resolveHand();
        announceNextHandIfAny(ai);
    }

    private void revealDealtCard(Card cardHit) {
        io.showDealerDealingCardMessage();
        utils.pauseForEffect(1000);
        io.printRevealedCardNotification(cardHit);
        utils.pauseForEffect(1000);
    }

    private void announceNextHandIfAny(Player ai){
        if (ai.hasUnresolvedHand()){
            utils.pauseForEffect(1000);
            io.displayNextHandMessage();
            utils.pauseForEffect(1000);
            io.printAICards(ai);
            utils.pauseForEffect(1000);
        }
    }
}
