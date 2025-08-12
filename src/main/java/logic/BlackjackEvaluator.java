package logic;

import model.Dealer;
import model.Outcome;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlackjackEvaluator {
    private PlayerManager playerManager;
    private Dealer dealer;

    public BlackjackEvaluator(PlayerManager playerManager, Dealer dealer){
        this.playerManager = playerManager;
        this.dealer = dealer;
    }

    public List<Player> getPlayersWithBlackjack(){
        List<Player> playersWithBlackjackList = new ArrayList<>();
        for (Player p : playerManager.getAllPlayers()){
            if (p.getHandTotalBlackJackValue() == 21){
                playersWithBlackjackList.add(p);
            }
        }
        return playersWithBlackjackList;
    }

    public boolean checkForDealerBlackjack() {
        return dealer.getTotalBlackJackValue() == 21;
    }

    public Map<Player, Outcome> settleBlackJack(Map<Player, Outcome> playerOutcomeMap) {
        boolean dealerHasBlackjack = checkForDealerBlackjack();
        for (Player p : playerManager.getAllPlayers()) {
            if (dealerHasBlackjack) {
                if (p.getHandTotalBlackJackValue() == 21) {
                    playerOutcomeMap.put(p, Outcome.PUSH);
                } else {
                    playerOutcomeMap.put(p, Outcome.LOSE);
                }
                continue;
            }
            if (p.getHandTotalBlackJackValue() == 21) {
                playerOutcomeMap.put(p, Outcome.BLACKJACK);
            }
        }
        return playerOutcomeMap;
    }
}
