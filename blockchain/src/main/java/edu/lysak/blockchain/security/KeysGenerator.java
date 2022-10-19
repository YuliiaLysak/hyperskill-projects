package edu.lysak.blockchain.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeysGenerator {
    private final KeyPairGenerator keyGen;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeysGenerator(int keyLength) {
        try {
            this.keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            String errorMessage = "Error during KeyPairGenerator.getInstance()";
            System.out.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }

        this.keyGen.initialize(keyLength);
    }

    public void createKeys() {
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        KeysGenerator keysGenerator = new KeysGenerator(1024);
        keysGenerator.createKeys();
        keysGenerator.writeToFile("blockchain/src/main/resources/key-pair/publicKey", keysGenerator.getPublicKey().getEncoded());
        keysGenerator.writeToFile("blockchain/src/main/resources/key-pair/privateKey", keysGenerator.getPrivateKey().getEncoded());
    }

}
