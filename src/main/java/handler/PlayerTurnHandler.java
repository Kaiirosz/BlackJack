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
        TurnResult turnResult = TurnResult.CONTINUE;
        while (turnResult.equals(TurnResult.CONTINUE)) {
            io.displayPlayerOptions();
            Action action = Action.parseString(io.readLine());
            switch (action){
                case HIT:
                    turnResult = playerTurnLogic.hit(playerManager.getHumanPlayer());
                    Card cardHit = playerTurnLogic.getHitCard();
                    io.showDealerGivingCardMessage();
                    utils.pauseForEffect(1000);
                    io.printRevealedCardNotification(cardHit);
                    utils.pauseForEffect(1000);
                    io.printAllCards(playerManager, dealer);
                    break;
                case STAND:
                    turnResult = playerTurnLogic.stand(playerManager.getHumanPlayer());
                    break;
            }
        }
        if (turnResult.equals(TurnResult.BUST)){
            io.printPlayerBustNotification();
            playerTurnLogic.removePlayerFromRound(playerManager.getHumanPlayer()); //ask to spectate here?
        }
    }

}
