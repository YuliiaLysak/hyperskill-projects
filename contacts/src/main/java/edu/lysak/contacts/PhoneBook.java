package edu.lysak.contacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneBook {
    private List<Contact> contacts;
    private String filePath;
    private final String[] args;

    public PhoneBook(String[] args) {
        this.args = args;
    }

    public void init() {
        try {
            if (args.length > 0) {
                filePath = "contacts/src/main/resources/" + args[0];

                if (Files.exists(Path.of(filePath))) {
                    contacts = (List<Contact>) SerializationUtils.deserialize(filePath);
                    System.err.println("Loaded Phone Book from " + args[0] + ". File will be updated in case of new changes.");
                } else {
                    contacts = new ArrayList<>();
                    System.err.println("File " + args[0] + " doesn't exist. All changes will be saved to the newly created file.");
                }
            } else {
                contacts = new ArrayList<>();
                System.err.println("File is not provided - contacts will be kept in memory");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

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
        person.setBirthDate(birthDate);
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

    public void remove(Contact contact) {
        contacts.remove(contact);
    }

    public int count() {
        return contacts.size();
    }

    public List<Contact> getAll() {
        return List.copyOf(contacts);
    }

    public List<Contact> getSearchResult(String query) {
        return contacts.stream()
                .filter(it -> it.getConcatenatedFields().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void updateFile() throws IOException {
        if (filePath != null) {
            SerializationUtils.serialize(contacts, filePath);
        }
    }
}
