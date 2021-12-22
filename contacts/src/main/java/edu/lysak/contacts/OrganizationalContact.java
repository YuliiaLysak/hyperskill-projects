package edu.lysak.contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public class OrganizationalContact extends Contact implements Serializable {
    private String name;
    private String address;

    @Override
    public String getAllFieldsAsString() {
        return "name, address";
    }

    @Override
    public Map<String, String> getAllFieldsAsMap() {
        return Map.of("name", "Organization name",
                "address", "Address");
    }

    @Override
    public void editField(String fieldName, String newValue) {
        this.setEdited(LocalDateTime.now());
        switch (fieldName) {
            case "name" -> this.name = newValue;
            case "address" -> this.address = newValue;
            case "number" -> this.setPhoneNumber(newValue);
        }
    }

    @Override
    public String getFieldValue(String fieldName) {
        return switch (fieldName) {
            case "name" -> name;
            case "address" -> address;
            case "number" -> this.getPhoneNumber();
            default -> throw new IllegalArgumentException("No such field in organization");
        };
    }

    @Override
    public String getContactName() {
        return name;
    }

    @Override
    public String getConcatenatedFields() {
        return name + address + getPhoneNumber();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OrganizationalContact{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
