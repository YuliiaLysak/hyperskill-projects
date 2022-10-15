package edu.lysak.blockchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {

    private static final String FILENAME = "blockchain/src/main/resources/my-blockchain.txt";
    private static final int BLOCK_COUNT = 5;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SecureRandom secureRandom = new SecureRandom();
        String leadingZeros = getLeadingZeroString();

        Blockchain blockchain;
        if (isBlockchainFileExist()) {
            blockchain = loadFromFile();
            if (!blockchain.isValid()) {
                System.out.println("Blockchain from the file is invalid");
                return;
            }
        } else {
            blockchain = new Blockchain(secureRandom);
        }

        for (int i = 1; i < BLOCK_COUNT + 1; i++) {
            blockchain.generateBlock(i, leadingZeros);
            saveToFile(blockchain);
        }

        blockchain.getBlockchain().values().forEach(System.out::println);
        blockchain.isValid();
    }

    private static String getLeadingZeroString() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must start with: ");
        int leadingZeros = scanner.nextInt();
        return "0".repeat(leadingZeros);
    }

    private static boolean isBlockchainFileExist() {
        return Files.exists(Path.of(FILENAME));
    }

    private static Blockchain loadFromFile() throws IOException, ClassNotFoundException {
        return (Blockchain) SerializationUtils.deserialize(FILENAME);
    }

    private static void saveToFile(Blockchain blockchain) throws IOException {
        SerializationUtils.serialize(blockchain, FILENAME);
    }
}
