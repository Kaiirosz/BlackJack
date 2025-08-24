package logic;

import model.*;

public class PlayerTurnLogic implements TurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private Card hitCard;
    private static final int BLACKJACK_VALUE = 21;


    public PlayerTurnLogic(GameContext gameContext, RoundOutcome roundOutcome){
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
    }

    public TurnResult hit(Player humanPlayer){
        Hand firstHand = humanPlayer.getFirstHand();
        setHitCard(dealer.giveCard());
        humanPlayer.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        } else if (firstHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
            return stand(humanPlayer);
        }
        return TurnResult.CONTINUE;
    }

    public TurnResult stand(Player player){
        return TurnResult.STAND;
    }

    public TurnResult doubleDown(Player humanPlayer){
        Hand firstHand = humanPlayer.getFirstHand();
        betManager.doubleDownBet(humanPlayer, firstHand);
        setHitCard(dealer.giveCard());
        humanPlayer.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        }
        return stand(humanPlayer);
    }

    public void removePlayerFromRound(Player humanPlayer){
        Hand firstHand = humanPlayer.getFirstHand();
        int betMoney = humanPlayer.getHandBet(firstHand);
        int betOutcome = betManager.settleBetOutcome(firstHand.getHandOutcome(), betMoney);
        roundOutcome.addPlayerOutcome(humanPlayer, betOutcome);
        playerManager.removePlayerFromRound(humanPlayer);
    }

    public Card getHitCard(){
        return hitCard;
    }

    public void setHitCard(Card hitCard){
        this.hitCard = hitCard;
    }


}
