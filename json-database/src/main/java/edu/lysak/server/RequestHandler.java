package edu.lysak.server;

public class RequestHandler {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private final JsonDatabase jsonDatabase;

    public RequestHandler(JsonDatabase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public String getResponse(String command) {
        if ("exit".equals(command)) {
            return OK;
        }
        if (command.startsWith("set")) {
            return executeSetCommand(command);
        } else if (command.startsWith("get")) {
            return executeGetCommand(command);
        } else {
            return executeDeleteCommand(command);
        }
    }

    private String executeSetCommand(String command) {
        String s = command.substring("set ".length());
        int index = Integer.parseInt(s.substring(0, s.indexOf(" ")));
        String text = s.substring(s.indexOf(" ") + 1);
        if (jsonDatabase.set(index, text)) {
            return OK;
        } else {
            return ERROR;
        }
    }

    private String executeGetCommand(String command) {
        int index = getIndex(command);
        String text = jsonDatabase.get(index);
        if (text != null) {
            return text;
        } else {
            return ERROR;
        }
    }

    private String executeDeleteCommand(String command) {
        int index = getIndex(command);
        if (jsonDatabase.delete(index)) {
            return OK;
        } else {
            return ERROR;
        }
    }

    private int getIndex(String command) {
        String[] s = command.split(" ");
        return Integer.parseInt(s[1]);
    }
}
