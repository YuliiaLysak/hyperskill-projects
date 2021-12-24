package edu.lysak.fileserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MyServerSocket {
    private static final String PUT = "PUT";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private static final String SPACE = " ";

    private final String address;
    private final int port;
    private final FileStorage fileStorage;

    public MyServerSocket(String address, int port, FileStorage fileStorage) {
        this.address = address;
        this.port = port;
        this.fileStorage = fileStorage;
    }

    public void start() throws IOException {
        System.out.println("Server started!");
        while (true) {
            try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
                try (
                        Socket socket = server.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String request = input.readUTF();
//                    String request = new String(input.readAllBytes(), StandardCharsets.UTF_8);

                    String response;
                    if (request.startsWith(PUT)) {
                        response = processPutRequest(request);
                    } else if (request.startsWith(GET)) {
                        response = processGetRequest(request);
                    } else if (request.startsWith(DELETE)) {
                        response = processDeleteRequest(request);
                    } else if ("exit".equals(request)) {
                        break;
                    } else {
                        throw new IllegalArgumentException(request + " request is not supported");
                    }

//                    output.writeUTF(response);
                    output.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    private String processPutRequest(String request) throws IOException {
        String fileNameAndData = request.substring(PUT.length() + 1);
        String fileName = fileNameAndData.substring(0, fileNameAndData.indexOf(SPACE));
        String fileContent = fileNameAndData.substring(fileNameAndData.indexOf(SPACE) + 1);
        if (fileStorage.contains(fileName)) {
            return "403";
        }
        fileStorage.addFile(fileName, fileContent);
        return "200";
    }

    private String processGetRequest(String request) throws IOException {
        String fileName = request.substring(GET.length() + 1);
        if (fileStorage.contains(fileName)) {
            return "200 " + fileStorage.getFileContent(fileName);
        }
        return "404";
    }

    private String processDeleteRequest(String request) throws IOException {
        String fileName = request.substring(DELETE.length() + 1);
        if (fileStorage.deleteFile(fileName)) {
            return "200";
        }
        return "404";
    }
}
