package logic;

import model.*;

import java.util.List;

public class AITurnLogic implements TurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private final List<Player> aiPlayers;
    private Card hitCard;
    private static final int BLACKJACK_VALUE = 21;


    public AITurnLogic(GameContext gameContext, RoundOutcome roundOutcome) {
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
        aiPlayers = playerManager.getAIPlayersInRound();
    }

    public Action decideAction(Player ai, boolean isFirstAction) {
        HandStrength dealerHandStrength = getDealerHandStrength();
        HandStrength aiHandStrength = getAIHandStrength(ai);
        AIDecisionTable aiDecisionTable = new AIDecisionTable(aiHandStrength, dealerHandStrength, isFirstAction, ai.canAffordDoubleDown());
        return aiDecisionTable.getAction();
    }

    private HandStrength getDealerHandStrength() {
        int faceUpCardValue = dealer.getFaceUpCard().getBlackJackValue(0);
        return HandStrength.ofDealerValue(faceUpCardValue);
    }

    private HandStrength getAIHandStrength(Player ai) {
        int totalHandValue = ai.getHandTotalBlackJackValue(ai.getFirstHand());
        return HandStrength.ofPlayerValue(totalHandValue);
    }

    public TurnResult hit(Player ai){
        Hand firstHand = ai.getFirstHand();
        setHitCard(dealer.giveCard());
        ai.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        } else if (firstHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
            return stand(ai);
        }
        return TurnResult.CONTINUE;
    }

    public TurnResult stand(Player ai){
        return TurnResult.STAND;
    }

    public TurnResult doubleDown(Player ai){
        Hand firstHand = ai.getFirstHand();
        betManager.doubleDownBet(ai, firstHand);
        setHitCard(dealer.giveCard());
        ai.addCardToHand(hitCard, firstHand);
        if (firstHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            firstHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        }
        return stand(ai);
    }

    public void removePlayerFromRound(Player ai){
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
