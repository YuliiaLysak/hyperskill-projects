package edu.lysak.blockchain.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignedMessage {
    private final String messageId;
    private final String text;
    private final byte[] signature;
    private final PublicKey publicKey;

    public SignedMessage(String messageId, String text, String publicKeyFile, String privateKeyFile) {
        this.messageId = messageId;
        this.text = text;
        this.signature = sign(messageId, text, privateKeyFile);
        this.publicKey = getPublic(publicKeyFile);
    }

    private byte[] sign(String messageId, String text, String privateKeyFile) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(getPrivate(privateKeyFile));
            rsa.update(messageId.getBytes());
            rsa.update(text.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            System.err.println("Error during signing the message");
            throw new RuntimeException(e);
        }
    }

    private PrivateKey getPrivate(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            String errorMessage = "Error during getting private key from the file. " +
                    "Generate public/private keys with KeysGenerator.class";
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private PublicKey getPublic(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            String errorMessage = "Error during getting public key from the file. " +
                    "Generate public/private keys with KeysGenerator.class";
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getText() {
        return text;
    }
}
