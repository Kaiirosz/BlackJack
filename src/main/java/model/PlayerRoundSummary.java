package model;

public record PlayerRoundSummary(Player player, int totalBetResult) {

    public String getSummary() {
        return  player.getName() + "\n" +
                player.getAllHandsAndOutcomes() +
                "\nTotal Bet Result: " + totalBetResult / 100 +
                "\nNew Balance: " + player.getBalance() / 100 +
                "\n-----------------------------";
    }
}

