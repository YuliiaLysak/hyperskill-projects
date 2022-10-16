package edu.lysak.blockchain;

import java.security.SecureRandom;

public class Main {
    private static final int BLOCK_COUNT = 5;

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        SecureRandom secureRandom = new SecureRandom();
        BlockchainExecutor blockchainExecutor = new BlockchainExecutor(
                blockchain,
                secureRandom,
                BLOCK_COUNT
        );
        blockchainExecutor.execute();
    }
}
