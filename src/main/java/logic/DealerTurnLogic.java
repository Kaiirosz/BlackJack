package logic;

import model.*;

public class DealerTurnLogic {

    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private static final int BLACKJACK_VALUE = 21;

    public DealerTurnLogic(GameContext gameContext, RoundOutcome roundOutcome) {
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
    }

    public Action decideAction() {
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue < 17){
            return Action.HIT;
        }
        return Action.STAND;
    }

    public TurnResult hit() {
        dealer.hit();
        return TurnResult.CONTINUE;
    }

    public TurnResult stand() {
        return TurnResult.STAND;
    }

    public void settleDealerBJNoPlayerBJ(){
        int dealersTotalValue = dealer.getTotalBlackJackValue();
        if (dealersTotalValue == BLACKJACK_VALUE) {
            for (Player p : playerManager.getAllPlayersInRound()){
                Hand hand = p.getFirstHand();
                hand.setHandOutcome(Outcome.LOSE);
                int playerBet = hand.getBet();
                int betResult = betManager.settleBetOutcome(hand.getHandOutcome(), playerBet);
                roundOutcome.addPlayerOutcome(p, betResult);
            }
        }
    }

}
