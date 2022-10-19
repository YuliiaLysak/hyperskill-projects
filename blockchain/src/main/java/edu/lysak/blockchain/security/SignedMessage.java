package edu.lysak.blockchain.security;

import java.security.PublicKey;

public class SignedMessage extends SignedPayload {

    public SignedMessage(String messageId, String text, byte[] signature, PublicKey publicKey) {
        super(messageId, text, signature, publicKey);
    }
}
