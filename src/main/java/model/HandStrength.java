package model;

public enum HandStrength {
    STRONG,
    HIGH,
    MEDIUM,
    WEAK;

    public static HandStrength ofPlayerValue(int totalHandValue){
        if (totalHandValue > 16){
            return HandStrength.STRONG;
        }
        else if (totalHandValue > 11){
            return HandStrength.HIGH;
        }
        else if (totalHandValue > 8){
            return HandStrength.MEDIUM;
        }
        else {
            return HandStrength.WEAK;
        }
    }

    public static HandStrength ofDealerValue(int faceUpCardValue){
        if (faceUpCardValue > 8){
            return HandStrength.STRONG;
        }
        else if (faceUpCardValue > 6){
            return HandStrength.MEDIUM;
        }
        else {
            return HandStrength.WEAK;
        }
    }
}
