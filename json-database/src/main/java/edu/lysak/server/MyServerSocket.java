package edu.lysak.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {
    private final String address;
    private final int port;

    public MyServerSocket(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() throws IOException {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            try (
                    Socket socket = server.accept(); // accepting a new client
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String msg = input.readUTF(); // reading a message
                System.out.println("Received: " + msg);

                String response = "A record # 12 was sent!";
                output.writeUTF(response); // resend it to the client
                System.out.println("Sent: " + response);
            }
        }
    }
}
