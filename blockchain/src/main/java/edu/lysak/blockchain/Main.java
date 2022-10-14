package edu.lysak.blockchain;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        blockchain.generateBlock(1);
        blockchain.generateBlock(2);
        blockchain.generateBlock(3);
        blockchain.generateBlock(4);
        blockchain.generateBlock(5);

        blockchain.getBlockchain().values().forEach(System.out::println);
        System.out.println(blockchain.validate());
    }
}
