package logic;

import io.GameIO;
import model.Dealer;
import model.Deck;
import model.Player;

public class GameLogic {
    private final Player player;
    private final Dealer dealer;
    private final GameIO io;

    public GameLogic(GameIO io){
        player = new Player("Player", 2500);
        dealer = new Dealer(new Deck());
        this.io = io;
    }


}
