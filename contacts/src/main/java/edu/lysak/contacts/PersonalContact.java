package edu.lysak.contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonalContact extends Contact {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String gender;

    public PersonalContact() {
        this.setPerson(true);
        this.setCreated(LocalDateTime.now());
        this.setEdited(LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
