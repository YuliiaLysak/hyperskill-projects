package edu.lysak.blockchain;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Runnable {
    private final Blockchain blockchain;

    public Client(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        AtomicInteger messageId = new AtomicInteger();
        Thread currentThread = Thread.currentThread();
        while (!currentThread.isInterrupted()) {
            String message = String.format(
                    "[Client #%s] message #%s: %s",
                    currentThread.getId(),
                    messageId.incrementAndGet(),
                    UUID.randomUUID()
            );

            blockchain.addMessage(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
