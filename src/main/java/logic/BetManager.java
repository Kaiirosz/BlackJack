package logic;

import io.GameIO;
import model.Player;

public class BetManager {
    private final GameIO io;

    public BetManager(GameIO io) {
        this.io = io;
    }

    public int askAndPlaceBet(Player player) {
        while (true) {
            int bet = io.askForBetAmount(player.getMoney()); // IO handles prompt
            if (isValidBet(player, bet)) {
                player.setMoney(player.getMoney() - bet);
                io.showBetWasMadeMessage(bet);
                return bet;
            }
            io.showInvalidBetMessage();
        }
    }

    private boolean isValidBet(Player player, int bet) {
        int playerBalance = player.getMoney();
        return bet <= playerBalance || bet > 0;
    }
}
