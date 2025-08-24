package model;

public enum Action {
    HIT,
    STAND,
    DOUBLE_DOWN;

    public static Action parseString(String actionStr, boolean isFirstAction, boolean canAffordDoubleDown) {
        if (actionStr.equalsIgnoreCase("DOUBLE DOWN") && !isFirstAction || !canAffordDoubleDown) {
            throw new IllegalArgumentException("Double Down not allowed after first action");
        }
        for (Action a : Action.values()) {
            if (a.toString().equalsIgnoreCase(actionStr)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Non-parseable string!");
    }

    @Override
    public String toString(){
        return name().replace("_", " ");
    }

}
