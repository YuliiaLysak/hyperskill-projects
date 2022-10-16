package edu.lysak.blockchain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Block implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final long timestamp;
    private final String prevHash;

    private int magicNumber;
    private String hash;
    private List<String> data;
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

    public List<String> getData() {
        return data;
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

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public void setMinerId(long minerId) {
        this.minerId = minerId;
    }

    public String asStringForHash() {
        return "" + id
                + timestamp
                + magicNumber
                + prevHash
                + String.join("\n", data);
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
                "\nBlock data:" + getBlockDataString() +
                "\nBlock was generating for " + generationTime + " seconds";
    }

    private String getBlockDataString() {
        if (data.isEmpty()) {
            return " no messages";
        } else {
            StringBuilder blockData = new StringBuilder("\n");
            for (String d : data) {
                blockData.append(d).append("\n");
            }
            return blockData.deleteCharAt(blockData.length() - 1).toString();
        }
    }
}
