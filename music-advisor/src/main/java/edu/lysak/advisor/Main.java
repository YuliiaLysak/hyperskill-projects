package edu.lysak.advisor;


import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        OutputHandler outputHandler = new OutputHandler(getPageSize(args));
        AuthHttpServer server = new AuthHttpServer(
                getAccessServerPoint(args),
                getServerPath(args),
                outputHandler,
                new JsonParser(),
                HttpClient.newBuilder().build()
        );
        InputHandler inputHandler = new InputHandler(scanner, server, outputHandler);
        inputHandler.proceed();
    }

    private static String getAccessServerPoint(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-access".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "https://accounts.spotify.com";
    }

    private static String getServerPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-resource".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "https://api.spotify.com";
    }

    private static int getPageSize(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-page".equals(args[i]) && i + 1 < args.length) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return 5;
    }
}
