package handler;

import io.GameIO;
import logic.GameContext;
import logic.PlayerManager;
import logic.PlayerTurnLogic;
import model.*;
import utils.GameUtils;


public class PlayerTurnHandler implements TurnHandler {

    @Override
    public void handleTurn(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils) {

        PlayerManager playerManager = gameContext.getPlayerManager();
        Dealer dealer = gameContext.getDealer();
        PlayerTurnLogic playerTurnLogic = new PlayerTurnLogic(gameContext, roundOutcome);
        io.showPlayerTurn(playerManager.getHumanPlayer().getName());
        utils.pauseForEffect(1000);
        Player humanPlayer = playerManager.getHumanPlayer();
        boolean isFirstAction = true;
        boolean canAffordDoubleDown = humanPlayer.canAffordDoubleDown();
        TurnResult turnResult = TurnResult.CONTINUE;
        while (turnResult.equals(TurnResult.CONTINUE)) {
            io.displayPlayerOptions(isFirstAction,canAffordDoubleDown);
            Action action = getAction(io, isFirstAction, canAffordDoubleDown);
            switch (action){
                case HIT:
                    turnResult = playerTurnLogic.hit(humanPlayer);
                    Card cardHit = playerTurnLogic.getHitCard();
                    io.showDealerGivingCardMessage();
                    utils.pauseForEffect(1000);
                    io.printRevealedCardNotification(cardHit);
                    utils.pauseForEffect(1000);
                    io.printAllCards(playerManager, dealer);
                    break;
                case STAND:
                    turnResult = playerTurnLogic.stand(humanPlayer);
                    break;
                case DOUBLE_DOWN:
                    turnResult = playerTurnLogic.doubleDown(humanPlayer);
                    Card cardHit2 = playerTurnLogic.getHitCard();
                    io.showDealerGivingCardMessage();
                    utils.pauseForEffect(1000);
                    io.printRevealedCardNotification(cardHit2);
                    utils.pauseForEffect(1000);
                    break;
            }
            isFirstAction = false;
        }
        if (turnResult.equals(TurnResult.BUST)){
            io.printPlayerBustNotification();
            playerTurnLogic.removePlayerFromRound(playerManager.getHumanPlayer()); //ask to spectate here?
        }
    }

    private Action getAction(GameIO io, boolean isFirstAction, boolean canAffordDoubleDown){
        while (true){
            try {
                return Action.parseString(io.readLine(), isFirstAction, canAffordDoubleDown);
            } catch (IllegalArgumentException e) {
                io.displayInvalidActionMessage();
            }
        }
    }

}
