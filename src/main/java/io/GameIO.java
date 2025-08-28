package io;

import logic.PlayerManager;
import model.*;

public interface GameIO {

    void println(String message);
    void printMoney(int money);
    String readLine();
    int readInt();

    void displayGameTitle();
    void displayGameStartBanner();

    String askForPlayerName();
    int askForNumberOfAIPlayers();
    void showInvalidNumberOfAIPlayersMessage();

    int askForBetAmount(int playerBalance);
    void showInvalidBetMessage();
    void showBetWasMadeMessage(int bet);
    void showAIBetMadeMessage(Player ai, int aiBet);

    void displayRoundStart();
    void showDealerIsShufflingTheDeckMessage();
    void showCardsDealtMessage();
    void printAcquiredCardNotification(Card firstCard, Card secondCard, Player player);
    void printDealersFaceUpCardNotification(Card faceUpCard);

    void printBlackjackNotification(Player p);

    void showDealerRevealingCardMessage();
    void printDealersBlackjackPair(Card faceUpCardNotation, Card faceDownCard);

    void printAllCards(PlayerManager playerManager, Dealer dealer);

    void showPlayerTurn(String name);
    void displayPlayerOptions(boolean isFirstAction, boolean canAffordDoubleDown, boolean canSplit);
    void displayInvalidActionMessage();

    void showDealerGivingCardMessage();
    void printRevealedCardNotification(Card hitCard);
    void displayCardsSplitMessage(Card pairCard);
    void printHittingForSplitHandNotification();
    void displayNextHandMessage();

    void printAIHitsNotification(Player ai);
    void printAIDoublesDownNotification(Player ai);
    void printAISplitsNotification(Player ai);
    void printAICards(Player ai);
    void printStandNotification();
    void printPlayerBustNotification();

    void showDealerHitsMessage();
    void printDealersTotalValue(int dealersTotalValue);

    void printDealerBustsMessage();

    void printRoundSummaryNotification();
    void printPlayerRoundSummary(String summary);
    void showGameOverMessage();
    void displayOutcomeMessage(Outcome outcome, int betMoney);
}
