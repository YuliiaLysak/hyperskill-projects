package edu.lysak.server;

import edu.lysak.protocol.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final ResponseHandler responseHandler;
    private final ServerSocket server;
    private final Socket socket;

    public ClientHandler(ResponseHandler responseHandler, ServerSocket server, Socket socket) {
        this.responseHandler = responseHandler;
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                socket;
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String command = input.readUTF(); // reading a message
            System.out.println("Received: " + command);

            Request request = responseHandler.getRequest(command);
            String jsonResponse = responseHandler.getJsonResponse(request);
            output.writeUTF(jsonResponse); // resend it to the client
            System.out.println("Sent: " + jsonResponse);

            if ("exit".equals(request.getType())) {
                server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
