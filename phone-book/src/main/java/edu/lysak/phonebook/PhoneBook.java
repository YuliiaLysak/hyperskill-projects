package edu.lysak.phonebook;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {
    private final List<PhoneContact> contacts = new ArrayList<>();

    public void loadContacts(String directoryFileName) {
        try {
            URI uri = getClass().getClassLoader().getResource(directoryFileName).toURI();
            List<String> directory = Files.readAllLines(Path.of(uri));
            directory
                    .stream()
                    .map(String::trim)
                    .forEach(it -> contacts.add(parsePhoneContact(it)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SearchResult searchPhoneNumbers(String findFileName) {
        List<String> contactsToFind;
        long found;
        try {
            URI uri = getClass().getClassLoader().getResource(findFileName).toURI();
            contactsToFind = Files.readAllLines(Path.of(uri));
            found = contactsToFind.stream()
                    .map(String::trim)
                    .filter(this::isPresent)
                    .count();
            return new SearchResult(contactsToFind.size(), (int) found);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SearchResult();
    }

    private boolean isPresent(String fullName) {
        return contacts.stream()
                .anyMatch(it -> fullName.equals(it.getFullName()));
    }

    private PhoneContact parsePhoneContact(String line) {
        Pattern contactPattern = Pattern.compile("(?<phoneNumber>\\d+)\\s(?<fullName>.+)");
        Matcher matcher = contactPattern.matcher(line);
        if (matcher.find()) {
            String phoneNumber = matcher.group("phoneNumber");
            String fullName = matcher.group("fullName");
            return new PhoneContact(fullName, phoneNumber);
        }
        return new PhoneContact();
    }
}
