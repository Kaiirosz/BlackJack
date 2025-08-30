package logic;

import handler.RoundHandler;
import io.GameIO;
import model.*;
import utils.GameUtils;

import java.util.List;

public class GameLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final GameContext gameContext;
    private final GameIO io;
    private final GameUtils utils;
    private int highScore;

    public GameLogic(GameIO io){
        gameContext = new GameContext();
        utils = new GameUtils();
        playerManager = gameContext.getPlayerManager();
        dealer = gameContext.getDealer();
        betManager = gameContext.getBetManager();
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
        while (numberOfAIPlayers > 6 || numberOfAIPlayers < 0){
            numberOfAIPlayers = io.askForNumberOfAIPlayers();
        }
        playerManager.initializeAIPlayers(numberOfAIPlayers);
    }

    private void continueGameUntilPlayerHasNoMoney(){
        Player humanPlayer;
        do {
            playerManager.removeAIPlayersOutOfMoney();
            playerManager.initializePlayersInRound();
            humanPlayer = playerManager.getHumanPlayer();
            betMoney();
            RoundHandler roundHandler = new RoundHandler(gameContext, io, utils);
            RoundOutcome outcome = roundHandler.startRound();
            utils.pauseForEffect(1500);
            io.printRoundSummaryNotification();
            utils.pauseForEffect(1000);
            for (Player p : outcome.getPlayerRoundSummaryMap().keySet()){
                int totalBetResult = outcome.getPlayerTotalBetResult(p);
                p.addMoney(totalBetResult);
                String summary = outcome.getPlayerRoundSummary(p).getSummary();
                io.printPlayerRoundSummary(summary);
            }
            dealer.returnCardsToDeck(playerManager);
            outcome.clear();
            utils.pauseForEffect(2000);
            updateHighScore(humanPlayer.getBalance());
        }
        while (humanPlayer.getBalance() > 0);
        io.showGameOverMessage();
        io.displayHighScore(highScore);
    }

    private void betMoney(){
        Player humanPlayer = playerManager.getHumanPlayer();
        int playerBalance = humanPlayer.getBalance();
        int bet;
        while (true){
            bet = io.askForBetAmount(playerBalance);
            if (betManager.isValidBet(playerBalance, bet)){
                Hand newHand = new Hand();
                humanPlayer.addHand(newHand);
                int betAmountInCents = bet * 100;
                betManager.placePlayerBet(humanPlayer, betAmountInCents, newHand);
                io.showBetWasMadeMessage(bet);
                break;
            }
            io.showInvalidBetMessage();
        }
        List<Player> aiPlayers = playerManager.getAIPlayersInRound();
        int betAmountInCents = bet * 100;
        for (Player ai: aiPlayers){
            Hand newHand = new Hand();
            ai.addHand(newHand);
            int aiBet = betManager.placeAIBet(ai, betAmountInCents, newHand);
            io.showAIBetMadeMessage(ai, aiBet);
            utils.pauseForEffect(500);
        }
    }

    private void updateHighScore(int playerCurrentBalance){
        int defaultStartingBalance = 500000;
        if (highScore == 0){
            highScore = defaultStartingBalance;
        }
        if (playerCurrentBalance > highScore){
            highScore = playerCurrentBalance;
        }
    }



}
