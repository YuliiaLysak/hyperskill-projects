package edu.lysak.blockchain.security;

import java.security.PublicKey;

public abstract class SignedPayload {

    private final String payloadId;
    private final String payload;
    private final byte[] signature;
    private final PublicKey publicKey;

    public SignedPayload(String payloadId, String payload, byte[] signature, PublicKey publicKey) {
        this.payloadId = payloadId;
        this.payload = payload;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public String getPayload() {
        return payload;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
