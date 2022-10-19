package edu.lysak.blockchain;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockchainExecutor {
    private final Blockchain blockchain;
    private final SecureRandom secureRandom;
    private final CountDownLatch countDownLatch;
    private final int minerCount;

    public BlockchainExecutor(
            Blockchain blockchain,
            SecureRandom secureRandom,
            int minerCount
    ) {
        this.blockchain = blockchain;
        this.secureRandom = secureRandom;
        this.minerCount = minerCount;
        this.countDownLatch = new CountDownLatch(minerCount);
    }

    public void execute() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < minerCount; i++) {
            executor.submit(new Miner(blockchain, secureRandom, countDownLatch));
        }
        try {
            countDownLatch.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
