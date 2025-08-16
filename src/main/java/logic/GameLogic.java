package logic;

import handler.RoundHandler;
import io.GameIO;
import model.*;
import utils.GameUtils;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private final Dealer dealer;
    private final PlayerManager playerManager;
    private final BetManager betManager;
    private final GameContext gameContext;
    private final GameIO io;
    private final GameUtils utils;

    public GameLogic(GameIO io){
        gameContext = new GameContext();
        playerManager = gameContext.getPlayerManager();
        dealer = gameContext.getDealer();
        betManager = gameContext.getBetManager();
        utils = new GameUtils();
        this.io = io;
    }

    public void start(){
        io.displayGameTitle();
        initializePlayers();
        io.displayGameStartBanner();
        continueGameUntilPlayerHasNoMoney();
    }

    private void initializePlayers(){
        String humanName = io.askForPlayerName();
        playerManager.initializeHumanPlayer(humanName);
        int numberOfAIPlayers = io.askForNumberOfAIPlayers();
        playerManager.initializeAIPlayers(numberOfAIPlayers);
    }

    private void continueGameUntilPlayerHasNoMoney(){
        do {
            betMoney();
            RoundHandler roundHandler = new RoundHandler(gameContext, io, utils);
            RoundOutcome outcome = roundHandler.startRound();
            utils.pauseForEffect(1500);
            int betResult = betManager.settleBetOutcome(outcome, betMoney);
            player.addMoney(betResult);
            io.displayOutcomeMessage(outcome, betResult);
            utils.pauseForEffect(2000);
        }
        while (player.getMoney() > 0);
    }

    private void betMoney(){
        Player humanPlayer = playerManager.getHumanPlayer();
        int bet;
        while (true){
            bet = io.askForBetAmount(humanPlayer.getMoney());
            if (betManager.isValidBet(humanPlayer, bet)){
                betManager.placePlayerBet(humanPlayer, bet);
                io.showBetWasMadeMessage(bet);
                break;
            }
            io.showInvalidBetMessage();
        }
        List<Player> aiPlayers = playerManager.getAIPlayers();
        for (Player ai: aiPlayers){
            int aiBet = betManager.placeAIBet(ai, bet);
            io.showAIBetMadeMessage(ai, aiBet);
            utils.pauseForEffect(500);
        }
    }

}
