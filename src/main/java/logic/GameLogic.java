package logic;

import io.GameIO;
import model.*;
import utils.GameUtils;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private final Dealer dealer;
    private final PlayerManager playerManager;
    private final BetManager betManager;
    private final GameIO io;
    private final GameUtils utils;

    public GameLogic(GameIO io){
        this.io = io;
        dealer = new Dealer(new Deck());
        playerManager = new PlayerManager(this.io, new ArrayList<>());
        betManager = new BetManager(this.io);
        utils = new GameUtils();
    }

    public void start(){
        io.displayGameTitle();
        initializePlayers();
        io.displayGameStartBanner();
        continueGameUntilPlayerHasNoMoney();
    }

    private void initializePlayers(){
        playerManager.initializeHumanPlayer();
        playerManager.initializeAIPlayers();
    }

    private void continueGameUntilPlayerHasNoMoney(){
        do {
            betMoney();
            Round round = new Round(io, playerManager, dealer, utils);
            RoundOutcome outcome = round.startRound();
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
