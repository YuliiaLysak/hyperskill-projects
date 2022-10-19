package edu.lysak.blockchain;

import edu.lysak.blockchain.security.KeysGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int BLOCK_COUNT = 15;
    private static final String PUBLIC_KEY_FILE = "blockchain/src/main/resources/key-pair/publicKey";
    private static final String PRIVATE_KEY_FILE = "blockchain/src/main/resources/key-pair/privateKey";

    public static void main(String[] args) throws IOException {
        generateKeys();

        Blockchain blockchain = new Blockchain();
        SecureRandom secureRandom = new SecureRandom();
//        activateClients(blockchain);
        BlockchainExecutor blockchainExecutor = new BlockchainExecutor(
                blockchain,
                secureRandom,
                BLOCK_COUNT
        );
        blockchainExecutor.execute();

        deleteKeys();
    }

    private static void generateKeys() throws IOException {
        KeysGenerator keysGenerator = new KeysGenerator(1024);
        keysGenerator.createKeys();
        keysGenerator.writeToFile(PUBLIC_KEY_FILE, keysGenerator.getPublicKey().getEncoded());
        keysGenerator.writeToFile(PRIVATE_KEY_FILE, keysGenerator.getPrivateKey().getEncoded());
    }

    private static void deleteKeys() throws IOException {
        Files.delete(Path.of(PUBLIC_KEY_FILE));
        Files.delete(Path.of(PRIVATE_KEY_FILE));
        Files.delete(Path.of("blockchain/src/main/resources/key-pair"));
    }

    private static void activateClients(Blockchain blockchain) {
        ExecutorService clientExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        clientExecutor.submit(new MessageClient(blockchain));
    }
}
