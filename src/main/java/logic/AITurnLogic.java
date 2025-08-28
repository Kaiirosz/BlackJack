package logic;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class AITurnLogic implements TurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private final Player ai;
    private final Hand currentHand;
    private final List<Card> cardsHitList;
    private static final int BLACKJACK_VALUE = 21;


    public AITurnLogic(GameContext gameContext, RoundOutcome roundOutcome, Player ai, Hand currentHand) {
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
        this.ai = ai;
        this.currentHand = currentHand;
        this.cardsHitList = new ArrayList<>();
    }

    public Action decideAction(boolean isFirstAction) {
        HandStrength dealerHandStrength = getDealerHandStrength();
        HandStrength aiHandStrength = getAIHandStrength();
        boolean canAffordDoubleDown = ai.canAffordDoubleDown(currentHand);
        boolean shouldSplit = currentHand.splitPairIsEightOrAces();
        AIDecisionTable aiDecisionTable = new AIDecisionTable(aiHandStrength, dealerHandStrength, isFirstAction, canAffordDoubleDown, shouldSplit);
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
        cardsHitList.add(dealer.giveCard());
        ai.addCardToHand(getLastCardHit(), currentHand);
        if (currentHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            currentHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        } else if (currentHand.getTotalBlackJackValue() == BLACKJACK_VALUE) {
            return stand();
        }
        return TurnResult.CONTINUE;
    }

    public TurnResult stand(){
        return TurnResult.STAND;
    }

    public TurnResult doubleDown(){
        betManager.doubleDownBet(ai, currentHand);
        cardsHitList.add(dealer.giveCard());
        ai.addCardToHand(getLastCardHit(), currentHand);
        if (currentHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            currentHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        }
        return stand();
    }

    public TurnResult split(){
        Hand splitHand = ai.splitHand(currentHand);
        betManager.placePlayerBet(ai, currentHand.getBet(), splitHand);
        cardsHitList.add(dealer.giveCard());
        ai.addCardToHand(getLastCardHit(), currentHand);
        cardsHitList.add(dealer.giveCard());
        ai.addCardToHand(getLastCardHit(), splitHand);
        return TurnResult.CONTINUE;
    }

    public void resolveBust(){
        int betMoney = ai.getHandBet(currentHand);
        int betOutcome = betManager.settleBetOutcome(currentHand.getHandOutcome(), betMoney);
        roundOutcome.addPlayerOutcome(ai, betOutcome);
        if (ai.allHandsHaveOutcomes()) {
            playerManager.removePlayerFromRound(ai);
        }
    }

    public void resolveHand(){
        currentHand.setUnresolved(false);
    }

    public Card getSecondToLastCardHit(){
        return cardsHitList.get(cardsHitList.size() - 2);
    }

    public Card getLastCardHit(){
        return cardsHitList.getLast();
    }

}
