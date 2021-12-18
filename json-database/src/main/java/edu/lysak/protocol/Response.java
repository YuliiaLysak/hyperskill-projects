package edu.lysak.protocol;

public class Response {
    private String response;
    private String value;
    private String reason;

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
