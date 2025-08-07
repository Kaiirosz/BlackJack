package logic;

import io.GameIO;
import model.*;
import utils.GameUtils;

public class GameLogic {
    private Player player;
    private final Dealer dealer;
    private final GameIO io;
    private final BetManager betManager;
    private final GameUtils utils;

    public GameLogic(GameIO io){
        dealer = new Dealer(new Deck());
        utils = new GameUtils();
        this.io = io;
        betManager = new BetManager(this.io);
    }

    public void start(){
        io.displayGameTitle();
        initializePlayer();
        io.displayGameStartBanner();
        continueGameUntilPlayerHasNoMoney();
    }

    private void initializePlayer(){
        String playerName = io.askForPlayerName();
        player = new Player(playerName, 250000);
    }

    private void continueGameUntilPlayerHasNoMoney(){
        do {
            int betMoney = betMoney();
            Round round = new Round(io, player, dealer, utils);
            Outcome outcome = round.startRound();
            int betResult = betManager.settleBetOutcome(outcome, betMoney);
            player.addMoney(betResult);
            utils.pauseForEffect(1500);
            io.displayOutcomeMessage(outcome, betResult);
            utils.pauseForEffect(2000);
            RoundResult roundResult = new RoundResult(outcome, betResult);
        }
        while (player.getMoney() > 0);
    }

    private int betMoney(){
        return betManager.askAndPlaceBet(player);
    }

}
