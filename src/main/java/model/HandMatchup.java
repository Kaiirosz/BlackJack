package model;

import java.util.*;

public class HandMatchup {
    private final HandStrength player, dealer;
    private final Map<Action, Integer> actionsMap;
    private final Random random;

    public HandMatchup(HandStrength player, HandStrength dealer){
        this.player = player;
        this.dealer = dealer;
        actionsMap = new LinkedHashMap<>();
        random = new Random();
    }

    public HandMatchup withAction(Action action, int odds){
        int sum = actionsMap.values()
                .stream()
                .mapToInt(s -> s)
                .sum();
        if (sum + odds > 100 || odds <= 0){
            throw new IllegalArgumentException("Invalid weight");
        }
        actionsMap.put(action, odds);
        return this;
    }

    public Action getAction(){
        int total = actionsMap.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (total != 100) {
            throw new IllegalStateException("Total weights must equal 100 before choosing an action");
        }
        int cumulative = 0;
        int rng = random.nextInt(100) + 1;
        for (Action a : actionsMap.keySet()){
            cumulative += actionsMap.get(a);
            if (rng <= cumulative){
                return a;
            }
        }
        throw new RuntimeException("Unknown Action");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandMatchup that)) return false;
        return player == that.player &&
                dealer == that.dealer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, dealer);
    }

    @Override
    public String toString() {
        return "HandMatchup{" +
                "dealer=" + dealer +
                ", player=" + player +
                ", random=" + random +
                ", actionsMap=" + actionsMap +
                '}';
    }
}
