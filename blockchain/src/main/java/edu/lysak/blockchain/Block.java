package edu.lysak.blockchain;

import java.io.Serial;
import java.io.Serializable;

public class Block implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final long timestamp;
    private final String prevHash;

    private int magicNumber;
    private String hash;
    private long generationTime;
    private long minerId;

    public Block(int id, long timestamp, String prevHash) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public void setMinerId(long minerId) {
        this.minerId = minerId;
    }

    public String asStringForHash() {
        return "" + id + timestamp + magicNumber + prevHash;
    }

    @Override
    public String toString() {
        return "Block:" +
                "\nCreated by miner # " + minerId +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nMagic number: " + magicNumber +
                "\nHash of the previous block:\n" + prevHash +
                "\nHash of the block:\n" + hash +
                "\nBlock was generating for " + generationTime + " seconds";
    }
}
