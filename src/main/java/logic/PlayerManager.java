package logic;

import model.Player;

import java.util.ArrayList;
import java.util.Iterator;
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
        humanPlayer = new Player(humanName, 500000, true);
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

    public void initializePlayersInRound(){
        playersInRound = new ArrayList<>(players);
    }


    public void removePlayerFromRound(Player p){
        playersInRound.remove(p);
    }


    public Player getHumanPlayer(){
        if (!checkIfHumanIsInRound()){
            throw new NoSuchElementException("There is no human player in the round");
        }
        return humanPlayer;
    }

    public boolean checkIfHumanIsInRound(){
        return playersInRound.contains(humanPlayer);
    }


    public List<Player> getAIPlayersInRound(){
        List<Player> aiPlayers = new ArrayList<>(playersInRound);
        aiPlayers.remove(humanPlayer);
        return aiPlayers;
    }

    public boolean checkIfAnAIStillInRound(){
        return !getAIPlayersInRound().isEmpty();
    }

    public void removeAIPlayersOutOfMoney(){
        Iterator<Player> it = players.iterator();
        while (it.hasNext()){
            Player p = it.next();
            if (p.getBalance() <= 0 && !p.getIsHuman()){
                it.remove();
            }
        }
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
