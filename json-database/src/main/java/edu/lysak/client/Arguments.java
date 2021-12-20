package edu.lysak.client;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = {"-t"}, description = "Type of the request")
    private String requestType;

    @Parameter(names = {"-k"}, description = "Key")
    private String key;

    @Parameter(names = "-v", description = "Value to save in the database (only in case of a 'set' request")
    private String value;

    @Parameter(names = {"-in"}, description = "Input file")
    private String inputFile;

    public String getRequestType() {
        return requestType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getInputFile() {
        return inputFile;
    }
}
