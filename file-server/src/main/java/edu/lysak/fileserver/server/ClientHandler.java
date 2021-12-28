package edu.lysak.fileserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static edu.lysak.fileserver.util.Constants.*;

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
            String request = input.readUTF();
            if ("exit".equals(request)) {
                server.close();
                return;
            }

            if (request.startsWith(PUT)) {
                int length = input.readInt();
                byte[] fileBytes = new byte[length];
                input.readFully(fileBytes, 0, fileBytes.length);
                String putResponse = responseHandler.processPutRequest(request, fileBytes);
                output.writeUTF(putResponse);

            } else if (request.startsWith(GET)) {
                Map<String, byte[]> responseMap = responseHandler.processGetRequest(request);
                for (var pair : responseMap.entrySet()) {
                    String code = pair.getKey();
                    byte[] fileBytes = pair.getValue();

                    output.writeUTF(code);
                    output.writeInt(fileBytes.length);
                    output.write(fileBytes);
                }

            } else if (request.startsWith(DELETE)) {
                String deleteResponse = responseHandler.processDeleteRequest(request);
                output.writeUTF(deleteResponse);
            } else {
                throw new IllegalArgumentException(request + " request is not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
