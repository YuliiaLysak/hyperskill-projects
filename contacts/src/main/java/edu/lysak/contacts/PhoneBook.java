package edu.lysak.contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class PhoneBook {
    private final List<Contact> contacts = new ArrayList<>();

    public void addPerson(
            String name,
            String surname,
            String birthDate,
            String gender,
            String phone
    ) {
        PersonalContact person = new PersonalContact();
        person.setName(name);
        person.setSurname(surname);
        person.setBirthDate(parseBirthDate(birthDate));
        person.setGender(gender);
        person.setPhoneNumber(phone);
        contacts.add(person);
    }

    public void addOrganization(
            String name,
            String address,
            String number
    ) {
        OrganizationalContact org = new OrganizationalContact();
        org.setName(name);
        org.setAddress(address);
        org.setPhoneNumber(number);
        contacts.add(org);
    }

    public void remove(int index) {
        contacts.remove(index);
    }

    public void editPerson(int index, String fieldName, String newValue) {
        PersonalContact person = (PersonalContact) contacts.get(index);
        person.setEdited(LocalDateTime.now());
        switch (fieldName) {
            case "name" -> person.setName(newValue);
            case "surname" -> person.setSurname(newValue);
            case "birth" -> person.setBirthDate(parseBirthDate(newValue));
            case "gender" -> person.setGender(getGender(newValue));
            case "number" -> person.setPhoneNumber(newValue);
        }
    }

    public void editOrganization(int index, String fieldName, String newValue) {
        OrganizationalContact org = (OrganizationalContact) contacts.get(index);
        org.setEdited(LocalDateTime.now());
        switch (fieldName) {
            case "name" -> org.setName(newValue);
            case "address" -> org.setAddress(newValue);
            case "number" -> org.setPhoneNumber(newValue);
        }
    }

    public int count() {
        return contacts.size();
    }

    public List<Contact> getAll() {
        return List.copyOf(contacts);
    }

    private LocalDate parseBirthDate(String birthDate) {
        try {
            return LocalDate.parse(birthDate);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    public boolean isPerson(int index) {
        Contact contact = contacts.get(index);
        return contact.isPerson();
    }

    public String getGender(String gender) {
        if ("M".equals(gender) || "F".equals(gender)) {
            return gender;
        }
        System.out.println("Bad gender!");
        return "";
    }
}
