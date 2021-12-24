package edu.lysak.fileserver.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MyClientSocket {
    private final String address;
    private final int port;

    public MyClientSocket(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String sendRequest(String request) throws IOException {
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF(request);
//            output.write(request.getBytes(StandardCharsets.UTF_8));

            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
//            return input.readUTF();
        }
    }
}
