package io;

import model.Card;
import model.Dealer;
import model.Outcome;
import model.Player;

public interface GameIO {

    void println(String message);
    void printMoney(int money);
    String readLine();
    int readInt();

    void displayGameTitle();
    void displayGameStartBanner();

    String askForPlayerName();

    int askForBetAmount(int playerBalance);
    void showInvalidBetMessage();
    void showBetWasMadeMessage(int bet);

    void displayRoundStart();
    void showDealerIsShufflingTheDeckMessage();
    void showCardsDealtMessage();
    void printAcquiredCardNotification(Card acquiredCard);
    void printDealersFaceUpCardNotification(Card faceUpCard);

    void printBlackjack();

    void showDealerRevealingCardMessage();
    void printDealersBlackjackPair(Card faceUpCardNotation, Card faceDownCard);

    void printPlayerCards(Player player, Dealer dealer);

    void displayPlayerOptions();

    void showDealerGivingCardMessage();
    void printRevealedCardNotification(Card hitCard);

    void showDealerHitsMessage();
    void printDealersTotalValue(int dealersTotalValue);

    void printDealerBustsMessage();

    void displayOutcomeMessage(Outcome outcome, int betMoney);
}
