package edu.lysak.blockchain.security;

import java.security.PublicKey;

public class SignedTransaction extends SignedPayload {

    public SignedTransaction(String transactionId, String transactionMessage, byte[] signature, PublicKey publicKey) {
        super(transactionId, transactionMessage, signature, publicKey);
    }
}
