package logic;

import io.GameIO;
import model.Dealer;
import model.Deck;
import model.Player;
import utils.GameUtils;

public class GameLogic {
    private Player player;
    private final Dealer dealer;
    private final GameIO io;
    private final GameUtils utils;

    public GameLogic(GameIO io){
        dealer = new Dealer(new Deck());
        utils = new GameUtils();
        this.io = io;
    }

    public void start(){
        io.println("Blackjack");
        io.println("Enter your name to start the game.");
        String playerName = io.readLine();
        Player player = new Player(playerName, 2500);
        io.println("Game Start");
        io.println("--------------");
        int betMoney = betMoney(player);
    }

    public int betMoney(Player player){
        int playerMoney = player.getMoney();
        int betMoney;
        while (true) {
            io.println("Enter the amount of money you want to bet:");
            io.printMoney(playerMoney);
            betMoney = io.readInt();
            if (betMoney > playerMoney || betMoney < 0) {
                io.println("Invalid amount of money, Try Again.");
                continue;
            }
            player.setMoney(playerMoney - betMoney);
            io.println(betMoney + " was put into the bet.");
            io.println("Remaining Money: " + player.getMoney());
            break;
        }
        return betMoney;
    }


}
