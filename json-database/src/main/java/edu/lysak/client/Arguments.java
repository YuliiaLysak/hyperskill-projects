package edu.lysak.client;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = {"-t"}, description = "Type of the request")
    private String requestType;

    @Parameter(names = {"-i"}, description = "Index of the cell")
    private int index;

    @Parameter(names = "-m", description = "Value to save in the database (only in case of a 'set' request")
    private String text;

    public String getRequestType() {
        return requestType;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }
}
