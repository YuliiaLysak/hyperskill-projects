package edu.lysak.fileserver.util;

public final class Constants {
    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 23456;

//    private static final String SERVER_STORAGE_FOLDER = System.getProperty("user.dir") + "/File Server/task/src/server/data/";
    public static final String SERVER_STORAGE_FOLDER = "file-server/src/main/resources/server/data/";

//    public static final String CLIENT_STORAGE_FOLDER = System.getProperty("user.dir") + "/File Server/task/src/client/data/";
    public static final String CLIENT_STORAGE_FOLDER = "file-server/src/main/resources/client/data/";

    public static final String PUT = "PUT";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";

    public static final String SUCCESS = "200";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";

    public static final String BY_ID = "BY_ID";
    public static final String BY_NAME = "BY_NAME";

    public static final String SPACE = " ";

    private Constants() {
    }
}
