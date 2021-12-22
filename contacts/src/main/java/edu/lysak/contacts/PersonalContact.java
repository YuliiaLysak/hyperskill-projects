package edu.lysak.contacts;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class PersonalContact extends Contact implements Serializable {
    private String name;
    private String surname;
    private String birthDate;
    private String gender;

    @Override
    public String getAllFieldsAsString() {
        return "name, surname, birth, gender";
    }

    @Override
    public Map<String, String> getAllFieldsAsMap() {
        return Map.of(
                "name", "Name",
                "surname", "Surname",
                "birth", "Birth date",
                "gender", "Gender");
    }

    @Override
    public void editField(String fieldName, String newValue) {
        this.setEdited(LocalDateTime.now());
        switch (fieldName) {
            case "name" -> this.name = newValue;
            case "surname" -> this.surname = newValue;
            case "birth" -> this.birthDate = newValue;
            case "gender" -> this.gender = newValue;
            case "number" -> this.setPhoneNumber(newValue);
        }
    }

    @Override
    public String getFieldValue(String fieldName) {
        return switch (fieldName) {
            case "name" -> name;
            case "surname" -> surname;
            case "birth" -> birthDate;
            case "gender" -> gender;
            case "number" -> getPhoneNumber();
            default -> throw new IllegalArgumentException("No such field in person");
        };
    }

    @Override
    public String getContactName() {
        return name + " " + surname;
    }

    @Override
    public String getConcatenatedFields() {
        return name + surname + birthDate + gender + getPhoneNumber();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(String birthDate) {
        try {
            LocalDate.parse(birthDate);
            this.birthDate = birthDate;
        } catch (DateTimeParseException exception) {
            this.birthDate = "[no data]";
        }
    }

    public void setGender(String gender) {
        if ("M".equals(gender) || "F".equals(gender)) {
            this.gender = gender;
        } else {
            this.gender = "[no data]";
        }
    }

    @Override
    public String toString() {
        return "PersonalContact{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
