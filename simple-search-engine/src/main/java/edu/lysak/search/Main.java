package edu.lysak.search;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        SearchEngine searchEngine = new SearchEngine();
        InputHandler inputHandler = new InputHandler(scanner, searchEngine);
        inputHandler.proceed(getFileName(args));
    }

    private static String getFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--data".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "";
    }
}
