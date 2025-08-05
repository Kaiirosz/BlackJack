package Main;

import io.ConsoleIO;
import io.GameIO;
import logic.GameLogic;

public class Main {
    public static void main(String[] args) {
        new GameLogic(new ConsoleIO()).start();
    }
}
