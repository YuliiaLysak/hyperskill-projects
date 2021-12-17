package edu.lysak.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {
    private final RequestHandler requestHandler;
    private final String address;
    private final int port;

    public MyServerSocket(RequestHandler requestHandler, String address, int port) {
        this.requestHandler = requestHandler;
        this.address = address;
        this.port = port;
    }

    public void run() throws IOException {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = server.accept(); // accepting a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String command = input.readUTF(); // reading a message
                    System.out.println("Received: " + command);

                    String response = requestHandler.getResponse(command);
                    output.writeUTF(response); // resend it to the client
                    System.out.println("Sent: " + response);

                    if ("exit".equals(command)) {
                        break;
                    }
                }
            }
        }
    }
}
