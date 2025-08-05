package logic;

import io.GameIO;
import model.Card;
import model.Dealer;
import model.Deck;
import model.Player;
import utils.GameUtils;

import java.util.List;

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
        player = new Player(playerName, 2500);
        io.println("Game Start");
        io.println("--------------");
        int betMoney = betMoney(player);
        startRound(betMoney);

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

    public void startRound(int betMoney){
        io.println("--------------");
        io.println("Round Started");
        io.println("The Dealer is shuffling the deck.");
        dealer.shuffleDeck();
        utils.pauseForEffect(1000);
        io.println("The cards are being dealt");
        utils.pauseForEffect(1000);
        dealer.dealCards(player);
        List<Card> playerCards = player.getAllCards();
        io.println("You got the card " + playerCards.getFirst().getCardNotation() + "!!");
        utils.pauseForEffect(1000);
        io.println("You got the card " + playerCards.get(1).getCardNotation() + "!!");
        utils.pauseForEffect(1000);
        io.println("The Dealer's face up card is " + dealer.getFaceUpCardNotation());
        utils.pauseForEffect(1000);
        io.println("--------------");
        io.println("Your cards: " + player.getDisplayedHand());
        io.println("The Dealer's cards: " + dealer.getCards());
        utils.pauseForEffect(2000);

        io.println("What do you do now?");
        io.println("1. Hit\n2. Stand");
        String action = io.readLine();

    }


}
