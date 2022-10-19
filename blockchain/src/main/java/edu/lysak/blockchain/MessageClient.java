package edu.lysak.blockchain;

import edu.lysak.blockchain.security.SignedMessage;
import edu.lysak.blockchain.security.SignedPayload;
import edu.lysak.blockchain.security.SigningUtils;

import java.security.PublicKey;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageClient implements Runnable {
    private final Blockchain blockchain;

    public MessageClient(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        try {
            AtomicInteger messageId = new AtomicInteger();
            Thread currentThread = Thread.currentThread();
            while (!currentThread.isInterrupted()) {
                SignedPayload signedMessage = buildSignedMessage(
                        String.valueOf(messageId.incrementAndGet()),
                        currentThread.getId()
                );
                blockchain.addMessage(signedMessage);

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Exception in MessageClient caused by:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private SignedPayload buildSignedMessage(String messageId, long threadId) {
        String text = String.format(
                "[Client #%s] message #%s: %s",
                threadId,
                messageId,
                UUID.randomUUID()
        );
        byte[] messageSignature = SigningUtils.signMessage(
                messageId,
                text,
                "blockchain/src/main/resources/key-pair/privateKey"
        );
        PublicKey messagePublicKey = SigningUtils.getPublic(
                "blockchain/src/main/resources/key-pair/publicKey"
        );
        return new SignedMessage(messageId, text, messageSignature, messagePublicKey);
    }
}
