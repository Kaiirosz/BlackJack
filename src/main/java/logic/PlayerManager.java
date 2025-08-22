package logic;

import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PlayerManager {

    private final List<Player> players;
    private static final int STARTING_BALANCE = 250000;
    private Player humanPlayer;
    private List<Player> playersInRound;

    public PlayerManager(List<Player> players){
        this.players = players;
    }



    public void initializeHumanPlayer(String humanName) {
        humanPlayer = new Player(humanName, STARTING_BALANCE, true);
        players.add(humanPlayer);
    }

    public void initializeAIPlayers(int numberOfAIPlayers) {
        int playerNumber = 2;
        for (int i = 0; i < numberOfAIPlayers; i++){
            String aiPlayerName = "Player " + playerNumber;
            players.add(new Player(aiPlayerName, STARTING_BALANCE, false));
            playerNumber++;
        }
    }

    public void checkForEligiblePlayers(){
        playersInRound = new ArrayList<>(players);
    }


    public void removePlayerFromRound(Player p){
        playersInRound.remove(p);
    }

    public Player getHumanPlayer(){
        if (!playersInRound.contains(humanPlayer)){
            throw new NoSuchElementException("There is no human player in the game");
        }
        return humanPlayer;
    }


    public List<Player> getAIPlayers(){
        List<Player> aiPlayers = new ArrayList<>(playersInRound);
        aiPlayers.remove(humanPlayer);
        return aiPlayers;
    }

    public boolean isEmpty(){
        return playersInRound.isEmpty();
    }

    public List<Player> getAllPlayersInRound(){
        return playersInRound;
    }

    public List<Player> getAllPlayers(){
        return players;
    }
}
