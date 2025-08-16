package logic;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlackjackEvaluator {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private static final int BLACKJACK_VALUE = 21;

    public BlackjackEvaluator(GameContext gameContext) {
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
    }

    public List<Player> getPlayersWithBlackjack() {
        List<Player> playersWithBlackjackList = new ArrayList<>();
        for (Player p : playerManager.getAllPlayers()) {
            Hand initialHand = p.getFirstHand();
            if (initialHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
                playersWithBlackjackList.add(p);
            }
        }
        return playersWithBlackjackList;
    }

    public boolean checkForDealerBlackjack() {
        return dealer.getTotalBlackJackValue() == BLACKJACK_VALUE;
    }

    public RoundOutcome settleBlackJack() {
        boolean dealerHasBlackjack = checkForDealerBlackjack();
        RoundOutcome roundOutcome = new RoundOutcome();
        for (Player p : playerManager.getAllPlayers()) {
            Hand initialHand = p.getFirstHand();
            int totalBetResult;
            if (dealerHasBlackjack) {
                if (initialHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
                    initialHand.setHandOutcome(Outcome.PUSH);
                } else {
                    initialHand.setHandOutcome(Outcome.LOSE);
                }
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
                continue;
            }
            if (initialHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
                initialHand.setHandOutcome(Outcome.BLACKJACK);
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
            }
        }
        return roundOutcome;
    }
}
