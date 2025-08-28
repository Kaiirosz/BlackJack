package logic;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerTurnLogic implements TurnLogic {
    private final PlayerManager playerManager;
    private final Dealer dealer;
    private final BetManager betManager;
    private final RoundOutcome roundOutcome;
    private final Player humanPlayer;
    private final Hand currentHand;
    private List<Card> cardsHitList;
    private static final int BLACKJACK_VALUE = 21;


    public PlayerTurnLogic(GameContext gameContext, RoundOutcome roundOutcome, Hand currentHand){
        this.playerManager = gameContext.getPlayerManager();
        this.dealer = gameContext.getDealer();
        this.betManager = gameContext.getBetManager();
        this.roundOutcome = roundOutcome;
        this.humanPlayer = playerManager.getHumanPlayer();
        this.currentHand = currentHand;
        this.cardsHitList = new ArrayList<>();
    }

    public TurnResult hit(){
        cardsHitList.add(dealer.giveCard());
        humanPlayer.addCardToHand(getLastCardHit(), currentHand);
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

    public TurnResult surrender(){
        currentHand.setHandOutcome(Outcome.SURRENDER);
        int handBet = currentHand.getBet();
        Outcome handOutcome = currentHand.getHandOutcome();
        int betOutcome = betManager.settleBetOutcome(handOutcome, handBet);
        roundOutcome.addPlayerOutcome(humanPlayer, betOutcome);
        playerManager.removePlayerFromRound(humanPlayer);
        return TurnResult.SURRENDER;
    }

    public TurnResult doubleDown(){
        betManager.doubleDownBet(humanPlayer, currentHand);
        cardsHitList.add(dealer.giveCard());
        humanPlayer.addCardToHand(getLastCardHit(), currentHand);
        if (currentHand.getTotalBlackJackValue() > BLACKJACK_VALUE) {
            currentHand.setHandOutcome(Outcome.BUST);
            return TurnResult.BUST;
        }
        return stand();
    }

    public TurnResult split(){
        Hand splitHand = humanPlayer.splitHand(currentHand);
        betManager.placePlayerBet(humanPlayer, currentHand.getBet(), splitHand);
        cardsHitList.add(dealer.giveCard());
        humanPlayer.addCardToHand(getLastCardHit(), currentHand);
        cardsHitList.add(dealer.giveCard());
        humanPlayer.addCardToHand(getLastCardHit(), splitHand);
        return TurnResult.CONTINUE;
    }



    public void resolveBust(){
        int betMoney = humanPlayer.getHandBet(currentHand);
        int betOutcome = betManager.settleBetOutcome(currentHand.getHandOutcome(), betMoney);
        roundOutcome.addPlayerOutcome(humanPlayer, betOutcome);
        if (humanPlayer.allHandsHaveOutcomes()) {
            playerManager.removePlayerFromRound(humanPlayer);
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
