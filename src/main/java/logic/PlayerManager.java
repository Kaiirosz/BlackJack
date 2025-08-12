package logic;

import io.GameIO;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PlayerManager {

    private final GameIO io;
    private final List<Player> players;
    private static final int STARTING_BALANCE = 250000;
    private Player humanPlayer;

    public PlayerManager(GameIO io, List<Player> players){
        this.io = io;
        this.players = players;
    }


    public void initializeHumanPlayer() {
        String playerName = io.askForPlayerName();
        humanPlayer = new Player(playerName, STARTING_BALANCE, true, new PlayerTurnStrategy());
        players.add(humanPlayer);

    }

    public void initializeAIPlayers() {
        int numberOfAIPlayers = io.askForNumberOfAIPlayers();
        for (int i = 0; i < numberOfAIPlayers; i++){
            String aiPlayerName = "Player " + i + 2;
            players.add(new Player(aiPlayerName, STARTING_BALANCE, false, new AITurnStrategy()));
        }
    }

    public void removePlayerFromRound(Player p){
        players.remove(p);
    }

    public Player getHumanPlayer(){
        if (humanPlayer == null){
            throw new NoSuchElementException("There is no human player in the game");
        }
        return humanPlayer;
    }

    public List<Player> getAIPlayers(){
        List<Player> aiPlayers = new ArrayList<>(players);
        aiPlayers.remove(humanPlayer);
        return aiPlayers;
    }

    public List<Player> getAllPlayers(){
        return players;
    }
}
