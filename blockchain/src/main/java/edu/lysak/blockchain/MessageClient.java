package edu.lysak.blockchain;

import edu.lysak.blockchain.security.SignedMessage;

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
                String id = String.valueOf(messageId.incrementAndGet());
                String text = String.format(
                        "[Client #%s] message #%s: %s",
                        currentThread.getId(),
                        id,
                        UUID.randomUUID()
                );
                SignedMessage signedMessage = new SignedMessage(
                        id,
                        text,
                        "blockchain/src/main/resources/key-pair/publicKey",
                        "blockchain/src/main/resources/key-pair/privateKey"
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
}
