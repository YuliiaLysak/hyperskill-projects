package edu.lysak.blockchain;

public class Block {
    private final int id;
    private final long timestamp;
    private String blockHash;
    private String prevBlockHash;

    public Block(int id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setPrevBlockHash(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public String asStringForHash() {
        return "" + id + timestamp + prevBlockHash;
    }

    @Override
    public String toString() {
        return "Block:" +
                "\nId: " + id +
                "\nTimestamp: " + timestamp +
                "\nHash of the previous block:\n" + prevBlockHash +
                "\nHash of the block:\n" + blockHash +
                '\n';
    }
}
