package logic;

import handler.RoundHandler;
import io.GameIO;
import model.*;
import utils.GameUtils;

import java.util.List;
import java.util.Set;

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
            playerManager.initializePlayersInRound();
            humanPlayer = playerManager.getHumanPlayer();
            betMoney(humanPlayer);
            RoundHandler roundHandler = new RoundHandler(gameContext, io, utils);
            RoundOutcome roundOutcome = roundHandler.startRound();
            printRoundSummary(roundOutcome);
            postRoundCleanUp(humanPlayer, roundOutcome);
        }
        while (humanPlayer.getBalance() > 0);
        io.showGameOverMessage();
        io.displayHighScore(highScore);
    }

    private void betMoney(Player humanPlayer){
        int bet = askAndPlaceHumanBet(humanPlayer);
        placeAIBets(bet);
    }

    private int askAndPlaceHumanBet(Player humanPlayer){
        int playerBalance = humanPlayer.getBalance();
        int bet;
        while (true){
            bet = io.askForBetAmount(playerBalance);
            if (betManager.isValidBet(playerBalance, bet)){
                Hand newHand = humanPlayer.addNewHand();
                int betAmountInCents = bet * 100;
                betManager.placePlayerBet(humanPlayer, betAmountInCents, newHand);
                io.showBetWasMadeMessage(bet);
                break;
            }
            io.showInvalidBetMessage();
        }
        return bet;
    }

    private void placeAIBets(int playerBet){
        List<Player> aiPlayers = playerManager.getAIPlayersInRound();
        int betAmountInCents = playerBet * 100;
        for (Player ai: aiPlayers){
            Hand newHand = ai.addNewHand();
            int aiBet = Math.min(betAmountInCents, ai.getBalance());
            betManager.placePlayerBet(ai, aiBet, newHand);
            io.showAIBetMadeMessage(ai, aiBet / 100);
            utils.pauseForEffect(500);
        }
    }

    private void printRoundSummary(RoundOutcome outcome){
        utils.pauseForEffect(1500);
        io.printRoundSummaryNotification();
        utils.pauseForEffect(1000);
        Set<Player> playerKeySet = outcome.getPlayerRoundSummaryMap().keySet();
        for (Player p : playerKeySet){
            int totalBetResult = outcome.getPlayerTotalBetResult(p);
            p.addMoney(totalBetResult);
            String summary = outcome.getPlayerSummary(p);
            io.printPlayerRoundSummary(summary);
        }
    }

    private void postRoundCleanUp(Player humanPlayer, RoundOutcome roundOutcome){
        playerManager.removeAIPlayersOutOfMoney();
        dealer.returnCardsToDeck(playerManager);
        roundOutcome.clear();
        utils.pauseForEffect(1500);
        updateHighScore(humanPlayer.getBalance());
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
