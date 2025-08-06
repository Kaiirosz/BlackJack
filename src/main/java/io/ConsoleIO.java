package io;

import java.util.Scanner;

public class ConsoleIO implements GameIO {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void printMoney(int money) {
        System.out.printf("Current Money: ₱%d%n", money / 100);
    }

    @Override
    public String readLine() {
        return sc.nextLine();
    }

    @Override
    public int readInt() {
        int input = sc.nextInt();
        sc.nextLine();
        return input;
    }
}
