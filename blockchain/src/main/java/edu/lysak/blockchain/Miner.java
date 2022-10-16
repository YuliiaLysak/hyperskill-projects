package edu.lysak.blockchain;

import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Miner implements Runnable {
    private final Blockchain blockchain;
    private final SecureRandom secureRandom;
    private final CountDownLatch countDownLatch;

    public Miner(Blockchain blockchain, SecureRandom secureRandom, CountDownLatch countDownLatch) {
        this.blockchain = blockchain;
        this.secureRandom = secureRandom;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        while (true) {
            Block block = generateBlock();
            block.setMinerId(Thread.currentThread().getId());
            boolean isAdded = blockchain.addBlock(block);
            if (isAdded) {
                break;
            }
        }

        countDownLatch.countDown();
    }

    private Block generateBlock() {
        long start = System.currentTimeMillis();
        long timeStamp = new Date().getTime();
        String prevHash = blockchain.getPrevHash();
        Block block = new Block(blockchain.nextId(), timeStamp, prevHash);

        String hash;
        do {
            block.setMagicNumber(secureRandom.nextInt());
            hash = StringUtil.applySha256(block.asStringForHash());
        } while (!hash.startsWith(blockchain.getLeadingZerosString()));
        block.setHash(hash);

        long end = System.currentTimeMillis();
        block.setGenerationTime(TimeUnit.MILLISECONDS.toSeconds(end - start));
        return block;
    }
}
