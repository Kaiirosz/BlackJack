package model;

public enum Action {
    HIT,
    STAND,
    DOUBLE_DOWN,
    SPLIT,
    SURRENDER;

    public static Action parseString(String actionStr, boolean isFirstAction, boolean canAffordDoubleDown, boolean canSplit, boolean hasSplit) {
        if (actionStr.equalsIgnoreCase("SPLIT") && !canSplit){
            throw new IllegalArgumentException("Split not allowed here");
        }

        if (actionStr.equalsIgnoreCase("SURRENDER") && !isFirstAction){
            throw new IllegalArgumentException("Surrender is only allowed on the first turn");
        }

        if (actionStr.equalsIgnoreCase("SURRENDER") && hasSplit){
            throw new IllegalArgumentException("Cannot surrender after splitting");
        }

        if (actionStr.equalsIgnoreCase("DOUBLE DOWN") && !isFirstAction) {
            throw new IllegalArgumentException("Double Down not allowed after first action");
        }

        if (actionStr.equalsIgnoreCase("DOUBLE DOWN") && !canAffordDoubleDown){
            throw new IllegalArgumentException("Can not afford double down");
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
