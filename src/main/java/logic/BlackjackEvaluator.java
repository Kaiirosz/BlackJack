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
        boolean dealerHasBlackjack = checkForDealerBlackjack();
        RoundOutcome roundOutcome = new RoundOutcome();
        int totalBetResult;
        for (Player p : playerManager.getAllPlayersInRound()) {
            Hand initialHand = p.getFirstHand();
            boolean initialHandHasBJ = initialHand.getTotalBlackJackValue() == BLACKJACK_VALUE;
            if (dealerHasBlackjack) {
                if (initialHandHasBJ) {
                    initialHand.setHandOutcome(Outcome.PUSH);
                } else {
                    initialHand.setHandOutcome(Outcome.LOSE);
                }
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
                continue;
            }
            if (initialHandHasBJ) {
                initialHand.setHandOutcome(Outcome.BLACKJACK);
                totalBetResult = betManager.settleBetOutcome(initialHand.getHandOutcome(), initialHand.getBet());
                roundOutcome.addPlayerOutcome(p, totalBetResult);
            }
        }
        return roundOutcome;
    }
}
