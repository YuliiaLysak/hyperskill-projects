package edu.lysak.blockchain;

import edu.lysak.blockchain.security.SignedMessage;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Blockchain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final long ONE_MINUTE = 60;
    private static final long FEW_SECONDS = 10;

    private final ConcurrentLinkedDeque<Block> blockchainDeque = new ConcurrentLinkedDeque<>();
    private final CopyOnWriteArrayList<SignedMessage> nonCommittedSignedMessages = new CopyOnWriteArrayList<>();
    private final AtomicInteger leadingZerosCount = new AtomicInteger(0);

    public String getPrevHash() {
        Block prevBlock = blockchainDeque.peekLast();
        return prevBlock == null ? "0" : prevBlock.getHash();
    }

    public int nextId() {
        return blockchainDeque.size() + 1;
    }

    public String getLeadingZerosString() {
        return "0".repeat(leadingZerosCount.get());
    }

    public synchronized boolean addBlock(Block block) {
        String leadingZeros = getLeadingZerosString();
        if (!isValidBlock(block, leadingZeros)) {
            return false;
        }

        blockchainDeque.offerLast(block);
        nonCommittedSignedMessages.removeAll(block.getData());

        int difficultyBefore = leadingZerosCount.get();
        long generationTime = block.getGenerationTime();
        if (generationTime > ONE_MINUTE) {
            leadingZerosCount.decrementAndGet();
        } else if (generationTime < FEW_SECONDS) {
            leadingZerosCount.incrementAndGet();
        }
        int difficultyAfter = leadingZerosCount.get();

        printBlock(block, difficultyBefore, difficultyAfter);
        return true;
    }

    private void printBlock(Block block, int difficultyBefore, int difficultyAfter) {
        System.out.println(block);
        if (difficultyAfter > difficultyBefore) {
            System.out.println("N was increased to " + difficultyAfter);
        } else if (difficultyAfter < difficultyBefore) {
            System.out.println("N was decreased by 1");
        } else {
            System.out.println("N stays the same");
        }
        System.out.println();
    }

    private boolean isValidBlock(Block currentBlock, String leadingZeros) {
        Block prevBlock = blockchainDeque.peekLast();
        String prevBlockHash = prevBlock == null ? "0" : prevBlock.getHash();
        String hash = currentBlock.getHash();
        String prevHash = currentBlock.getPrevHash();
        return prevHash.equals(prevBlockHash)
                && hash.startsWith(leadingZeros);
    }

    public List<SignedMessage> getNonCommittedMessages() {
        return List.copyOf(nonCommittedSignedMessages);
    }

    public synchronized void addMessage(SignedMessage signedMessage) {
        if (isValidMessage(signedMessage)) {
            nonCommittedSignedMessages.add(signedMessage);
        } else {
            System.err.println("Message signature is not valid");
        }
    }

    private boolean isValidMessage(SignedMessage signedMessage) {
        String currentMessageId = signedMessage.getMessageId();
        String prevMessageId = nonCommittedSignedMessages.size() == 0
                ? "0"
                : nonCommittedSignedMessages.get(nonCommittedSignedMessages.size() - 1).getMessageId();
        if (Integer.parseInt(currentMessageId) <= Integer.parseInt(prevMessageId)) {
            System.err.println("Invalid messageId - it should be in ascending order in the blockchain");
            return false;
        }

        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(signedMessage.getPublicKey());
            sig.update(signedMessage.getMessageId().getBytes());
            sig.update(signedMessage.getText().getBytes());
            return sig.verify(signedMessage.getSignature());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            System.err.println("Error during message signature verification");
            return false;
        }
    }

    // TODO: 17.10.2022 add method isValidBlockchain()
}
