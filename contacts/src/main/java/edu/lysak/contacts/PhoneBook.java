package edu.lysak.contacts;

import java.util.ArrayList;
import java.util.List;

public class PhoneBook {
    private final List<Contact> contacts = new ArrayList<>();

    public void add(String name, String surname, String phoneNumber) {
        contacts.add(new Contact(name, surname, phoneNumber));
    }

    public void remove(int index) {
        contacts.remove(index);
    }

    public void edit(int index, String fieldName, String newValue) {
        Contact contact = contacts.get(index);
        switch (fieldName) {
            case "name" -> contact.setName(newValue);
            case "surname" -> contact.setSurname(newValue);
            case "number" -> contact.setPhoneNumber(newValue);
        }
    }

    public int count() {
        return contacts.size();
    }

    public List<Contact> getAll() {
        return List.copyOf(contacts);
    }
}
