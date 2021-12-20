package edu.lysak.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MyClientSocket {
    private final RequestHandler requestHandler;
    private final String address;
    private final int port;

    public MyClientSocket(RequestHandler requestHandler, String address, int port) {
        this.requestHandler = requestHandler;
        this.address = address;
        this.port = port;
    }

    public void run(Arguments arguments) throws IOException {
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String jsonMessage = requestHandler.createRequest(arguments);
            output.writeUTF(jsonMessage); // sending message to the server
            System.out.println("Sent: " + jsonMessage);

//            String receivedMessage = new String(input.readAllBytes(), StandardCharsets.UTF_8); // response message
            String receivedMessage = input.readUTF(); // response message
            System.out.println("Received: " + receivedMessage);
        }
    }
}
