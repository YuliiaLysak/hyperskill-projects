package edu.lysak.blockchain;

public class MinerInfo {
    private final long minerId;
    private int virtualCoins;

    public MinerInfo(long minerId, int virtualCoins) {
        this.minerId = minerId;
        this.virtualCoins = virtualCoins;
    }

    public long getMinerId() {
        return minerId;
    }

    public int getVirtualCoins() {
        return virtualCoins;
    }

    public void setVirtualCoins(int virtualCoins) {
        this.virtualCoins = virtualCoins;
    }
}
