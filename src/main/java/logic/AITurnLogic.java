package logic;

import model.*;

import java.util.List;

public class AITurnLogic implements TurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private final Player ai;
    private final Hand currentHand;
    private Card hitCard;
    private static final int BLACKJACK_VALUE = 21;


    public AITurnLogic(GameContext gameContext, RoundOutcome roundOutcome, Player ai, Hand currentHand) {
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
        this.ai = ai;
        this.currentHand = currentHand;
    }

    public Action decideAction(boolean isFirstAction) {
        HandStrength dealerHandStrength = getDealerHandStrength();
        HandStrength aiHandStrength = getAIHandStrength();
        AIDecisionTable aiDecisionTable = new AIDecisionTable(aiHandStrength, dealerHandStrength, isFirstAction, ai.canAffordDoubleDown(currentHand));
        return aiDecisionTable.getAction();
    }

    private HandStrength getDealerHandStrength() {
        int faceUpCardValue = dealer.getFaceUpCard().getBlackJackValue(0);
        return HandStrength.ofDealerValue(faceUpCardValue);
    }

    private HandStrength getAIHandStrength() {
        int totalHandValue = ai.getHandTotalBlackJackValue(ai.getFirstHand());
        return HandStrength.ofPlayerValue(totalHandValue);
    }

    public TurnResult hit(){
        Hand firstHand = ai.getFirstHand();
        setHitCard(dealer.giveCard());
        ai.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        } else if (firstHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
            return stand();
        }
        return TurnResult.CONTINUE;
    }

    public TurnResult stand(){
        return TurnResult.STAND;
    }

    public TurnResult doubleDown(){
        Hand firstHand = ai.getFirstHand();
        betManager.doubleDownBet(ai, firstHand);
        setHitCard(dealer.giveCard());
        ai.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        }
        return stand();
    }

    public TurnResult split(){
        return TurnResult.CONTINUE;
    }

    public void removePlayerFromRound(){
        Hand firstHand = ai.getFirstHand();
        int betMoney = ai.getHandBet(firstHand);
        int betOutcome = betManager.settleBetOutcome(firstHand.getHandOutcome(), betMoney);
        roundOutcome.addPlayerOutcome(ai, betOutcome);
        playerManager.removePlayerFromRound(ai);
    }

    public Card getHitCard(){
        return hitCard;
    }

    public void setHitCard(Card hitCard){
        this.hitCard = hitCard;
    }

}
