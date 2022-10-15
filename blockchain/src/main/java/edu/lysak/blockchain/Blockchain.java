package edu.lysak.blockchain;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Blockchain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<Integer, Block> blockchain = new HashMap<>();

    private final SecureRandom secureRandom;

    public Blockchain(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public void generateBlock(int id, String leadingZeros) {
        long start = System.currentTimeMillis();
        long timeStamp = new Date().getTime();
        String prevHash = getPrevHash(id);
        Block block = new Block(id, timeStamp, prevHash);

        String hash;
        do {
            block.setMagicNumber(secureRandom.nextInt());
            hash = StringUtil.applySha256(block.asStringForHash());
        } while (!hash.startsWith(leadingZeros));
        block.setHash(hash);

        long end = System.currentTimeMillis();
        block.setGenerationTime(TimeUnit.MILLISECONDS.toSeconds(end - start));
        blockchain.put(id, block);
    }

    private String getPrevHash(int id) {
        if (id == 1) {
            return "0";
        } else {
            return blockchain.get(id - 1).getHash();
        }
    }

    public boolean isValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            String blockHash = blockchain.get(i).getHash();
            String prevBlockHash = blockchain.get(i + 1).getPrevHash();
            if (!blockHash.equals(prevBlockHash)) {
                return false;
            }
        }
        return true;
    }

    public Map<Integer, Block> getBlockchain() {
        return blockchain;
    }
}
