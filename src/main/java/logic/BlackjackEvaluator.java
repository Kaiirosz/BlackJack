package logic;

import model.*;

import java.util.ArrayList;
import java.util.List;

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
        for (Player p : playerManager.getAllPlayersInRound()) {
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
        RoundOutcome roundOutcome = new RoundOutcome();
        boolean dealerHasBJ = checkForDealerBlackjack();
        int totalBetResult;
        for (Player p : playerManager.getAllPlayersInRound()) {
            Hand initialHand = p.getFirstHand();
            boolean initialHandHasBJ = initialHand.getTotalBlackJackValue() == BLACKJACK_VALUE;
            if (dealerHasBJ) {
                if (initialHandHasBJ) {
                    initialHand.setHandOutcome(Outcome.PUSH);
                } else {
                    initialHand.setHandOutcome(Outcome.LOSE);
                }
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
                playerManager.removePlayerFromRound(p);
                continue;
            }
            if (initialHandHasBJ) {
                initialHand.setHandOutcome(Outcome.BLACKJACK);
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
                playerManager.removePlayerFromRound(p);
            }
        }

        return roundOutcome;
    }
}
