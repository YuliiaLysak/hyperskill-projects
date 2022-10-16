package edu.lysak.blockchain;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int BLOCK_COUNT = 5;

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        SecureRandom secureRandom = new SecureRandom();
        activateClients(blockchain);
        BlockchainExecutor blockchainExecutor = new BlockchainExecutor(
                blockchain,
                secureRandom,
                BLOCK_COUNT
        );
        blockchainExecutor.execute();
    }

    private static void activateClients(Blockchain blockchain) {
        ExecutorService clientExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        clientExecutor.submit(new Client(blockchain));
    }
}
