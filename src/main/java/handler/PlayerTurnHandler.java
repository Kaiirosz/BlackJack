package handler;

import io.GameIO;
import logic.GameContext;
import logic.PlayerManager;
import logic.PlayerTurnLogic;
import model.*;
import utils.GameUtils;


public class PlayerTurnHandler implements TurnHandler {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final Player humanPlayer;
    private final RoundOutcome roundOutcome;
    private final GameContext gameContext;
    private final GameIO io;
    private final GameUtils utils;

    public PlayerTurnHandler(GameContext gameContext, RoundOutcome roundOutcome, GameIO io, GameUtils utils){
        this.gameContext = gameContext;
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.roundOutcome = roundOutcome;
        this.humanPlayer = playerManager.getHumanPlayer();
        this.io = io;
        this.utils = utils;
    }

    @Override
    public void handleTurn() {
        io.showPlayerTurn(playerManager.getHumanPlayer().getName());
        utils.pauseForEffect(1000);
        while (humanPlayer.hasUnresolvedHand()){
            processAction();
        }
    }

    void processAction(){
        boolean isFirstAction = true;
        Hand currentHand = humanPlayer.getFirstUnresolvedHand();
        PlayerTurnLogic playerTurnLogic = new PlayerTurnLogic(gameContext, roundOutcome, currentHand);
        boolean canAffordDoubleDown = humanPlayer.canAffordDoubleDown(currentHand);
        boolean canSplit = humanPlayer.checkIfCanSplit(currentHand);
        TurnResult turnResult = TurnResult.CONTINUE;
        while (turnResult.equals(TurnResult.CONTINUE)) {
            io.displayPlayerOptions(isFirstAction,canAffordDoubleDown, canSplit);
            Action action = getAction(io, isFirstAction, canAffordDoubleDown, canSplit);
            Card cardHit;
            switch (action){
                case HIT:
                    turnResult = playerTurnLogic.hit();
                    cardHit = playerTurnLogic.getLastCardHit();
                    revealDealtCard(cardHit);
                    io.printAllCards(playerManager, dealer);
                    isFirstAction = false;
                    break;
                case STAND:
                    turnResult = playerTurnLogic.stand();
                    break;
                case DOUBLE_DOWN:
                    turnResult = playerTurnLogic.doubleDown();
                    cardHit = playerTurnLogic.getLastCardHit();
                    revealDealtCard(cardHit);
                    isFirstAction = false;
                    break;
                case SPLIT:
                    turnResult = playerTurnLogic.split();
                    io.displayCardsSplitMessage(currentHand.getCard(0));
                    utils.pauseForEffect(1000);
                    Card cardHitForFirstHand = playerTurnLogic.getSecondToLastCardHit();
                    Card cardHitForSecondHand = playerTurnLogic.getLastCardHit();
                    revealDealtCard(cardHitForFirstHand);
                    io.printHittingForSplitHandNotification();
                    revealDealtCard(cardHitForSecondHand);
                    io.printAllCards(playerManager,dealer);
            }
            canSplit = false;
        }
        if (turnResult.equals(TurnResult.BUST)){
            io.printPlayerBustNotification();
            playerTurnLogic.resolveBust();
        }
        playerTurnLogic.resolveHand();
        if (humanPlayer.hasUnresolvedHand()){
            utils.pauseForEffect(1000);
            io.displayNextHandMessage();
            utils.pauseForEffect(1000);
            io.printAllCards(playerManager, dealer);
        }
    }

    Action getAction(GameIO io, boolean isFirstAction, boolean canAffordDoubleDown, boolean canSplit){
        while (true){
            try {
                return Action.parseString(io.readLine(), isFirstAction, canAffordDoubleDown, canSplit);
            } catch (IllegalArgumentException e) {
                io.displayInvalidActionMessage();
            }
        }
    }

    private void revealDealtCard(Card cardHit){
        io.showDealerGivingCardMessage();
        utils.pauseForEffect(1000);
        io.printRevealedCardNotification(cardHit);
        utils.pauseForEffect(1000);
    }

}
