package model;

public record PlayerRoundSummary(Player player, int totalBetResult) {

    public PlayerRoundSummary(Player player, int totalBetResult) {
        this.player = player;
        this.totalBetResult = totalBetResult;
    }

    public String getSummary() {
        return  player.getName() + "\n" +
                player.getAllHandsAndOutcomes() +
                "\nTotal Bet Result: " + totalBetResult / 100 +
                "\n-----------------------------";
    }
}

