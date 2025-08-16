package logic;

import model.*;

public class PlayerTurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private final Player humanPlayer;
    private static final int BLACKJACK_VALUE = 21;


    public PlayerTurnLogic(GameContext gameContext, RoundOutcome roundOutcome){
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
        humanPlayer = playerManager.getHumanPlayer();
    }

    public boolean hit(){
        Card cardHit = dealer.giveCard();
        Hand firstHand = humanPlayer.getFirstHand();
        humanPlayer.addCardToHand(cardHit, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            int betMoney = humanPlayer.getHandBet(firstHand);
            int betOutcome = betManager.settleBetOutcome(Outcome.BUST, betMoney);
            roundOutcome.addPlayerOutcome(humanPlayer, betOutcome);
            playerManager.removePlayerFromRound(humanPlayer);
            return false;
        } else if (player.getHandTotalBlackJackValue() == BLACKJACK_VALUE) {
            return true;
        }
    }
}
