package edu.lysak.blockchain;

import edu.lysak.blockchain.security.SignedPayload;
import edu.lysak.blockchain.security.SignedTransaction;
import edu.lysak.blockchain.security.SigningUtils;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

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
            int minerId = (int) Thread.currentThread().getId();
            block.setMinerId(minerId);
            boolean isAdded = blockchain.addBlock(block);
            if (isAdded) {
                blockchain.addMinerInfo(new MinerInfo(minerId, 100));
                Optional<MinerInfo> senderMiner = blockchain.getMinerInfo(minerId);
                Optional<MinerInfo> recipientMiner = blockchain.getRecipientMinerInfo(minerId);

                if (senderMiner.isPresent() && recipientMiner.isPresent()) {
                    int spentCoins = spendVirtualCoins(senderMiner.get(), recipientMiner.get());
                    SignedPayload signedTransaction = buildSignedTransaction(
                            UUID.randomUUID().toString(),
                            senderMiner.get(),
                            recipientMiner.get(),
                            spentCoins
                    );
                    blockchain.addTransaction(signedTransaction);
                }
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
            setData(prevHash, block);
            block.setMagicNumber(secureRandom.nextInt());
            hash = StringUtil.applySha256(block.asStringForHash());
        } while (!hash.startsWith(blockchain.getLeadingZerosString()));
        block.setHash(hash);

        long end = System.currentTimeMillis();
        block.setGenerationTime(end - start);
        return block;
    }

    private void setData(String prevHash, Block block) {
        if ("0".equals(prevHash)) {
            block.setData(List.of());
        } else {
            block.setData(blockchain.getNonCommittedTransactions());
        }
    }

    private int spendVirtualCoins(MinerInfo senderMiner, MinerInfo recipientMiner) {
        int randomCoins = secureRandom.nextInt(senderMiner.getVirtualCoins());
        senderMiner.setVirtualCoins(senderMiner.getVirtualCoins() - randomCoins);
        recipientMiner.setVirtualCoins(recipientMiner.getVirtualCoins() + randomCoins);
        return randomCoins;
    }

    private SignedPayload buildSignedTransaction(
            String transactionId,
            MinerInfo senderMiner,
            MinerInfo recipientMiner,
            int spentCoins
    ) {
        String transactionMessage = String.format(
                "miner%s sent %s VC to miner%s",
                senderMiner.getMinerId(),
                spentCoins,
                recipientMiner.getMinerId()
        );
        byte[] signature = SigningUtils.signMessage(
                transactionId,
                transactionMessage,
                "blockchain/src/main/resources/key-pair/privateKey"
        );
        PublicKey publicKey = SigningUtils.getPublic(
                "blockchain/src/main/resources/key-pair/publicKey"
        );
        return new SignedTransaction(transactionId, transactionMessage, signature, publicKey);
    }
}
