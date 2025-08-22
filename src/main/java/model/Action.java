package model;

import java.util.IllegalFormatConversionException;

public enum Action {
    HIT,
    STAND;

    public static Action parseString(String actionStr){
        actionStr = actionStr.toUpperCase();
        for (Action a : Action.values()){
            if (a.toString().equalsIgnoreCase(actionStr)){
                return a;
            }
        }
        throw new IllegalArgumentException("Non-parseable string!");
    }
}
