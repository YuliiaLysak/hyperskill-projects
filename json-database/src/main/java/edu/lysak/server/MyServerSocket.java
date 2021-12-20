package edu.lysak.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServerSocket {
    private final ResponseHandler responseHandler;
    private final String address;
    private final int port;

    public MyServerSocket(ResponseHandler requestHandler, String address, int port) {
        this.responseHandler = requestHandler;
        this.address = address;
        this.port = port;
    }

    public void run() throws IOException {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            ExecutorService executor = Executors.newFixedThreadPool(4);
            while (!Thread.interrupted()) {
                Socket socket = acceptClientSocket(server);  // accepting a new client
                if (socket == null) {
                    executor.shutdownNow();
                    break;
                }

                executor.submit(() -> new ClientHandler(responseHandler, server, socket).run());
            }
        }
    }

    private Socket acceptClientSocket(ServerSocket server) {
        try {
            return server.accept();
        } catch (IOException e) {
            return null;
        }
    }
}
