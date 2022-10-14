package edu.lysak.blockchain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Blockchain {
    private final Map<Integer, Block> blockchain = new HashMap<>();

    public Map<Integer, Block> getBlockchain() {
        return blockchain;
    }

    public void generateBlock(int id) {
        long timeStamp = new Date().getTime();
        Block block = new Block(id, timeStamp);
        if (id == 1) {
            block.setPrevBlockHash("0");
        } else {
            String prevBlockHash = blockchain.get(id - 1).getBlockHash();
            block.setPrevBlockHash(prevBlockHash);
        }

        block.setBlockHash(StringUtil.applySha256(block.asStringForHash()));
        blockchain.put(id, block);
    }

    public boolean validate() {
        for (int i = 1; i < blockchain.size(); i++) {
            String blockHash = blockchain.get(i).getBlockHash();
            String prevBlockHash = blockchain.get(i + 1).getPrevBlockHash();
            if (!blockHash.equals(prevBlockHash)) {
                return false;
            }
        }
        return true;
    }
}
