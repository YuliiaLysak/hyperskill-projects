package edu.lysak.phonebook;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {
    private final List<PhoneContact> contacts = new ArrayList<>();
    private List<PhoneContact> sortedContacts;

    public PhoneBook(String directoryFileName) {
        loadContacts(directoryFileName);
    }

    public boolean bubbleSortPhoneNumbers(long beforeSort, long linearSearchTime) {
        sortedContacts = new ArrayList<>(List.copyOf(contacts));
        int n = sortedContacts.size();
        PhoneContact temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (sortedContacts.get(j - 1).compareTo(sortedContacts.get(j)) > 0) {
                    temp = sortedContacts.get(j - 1);
                    sortedContacts.set(j - 1, sortedContacts.get(j));
                    sortedContacts.set(j, temp);
                }
                long currentTime = System.currentTimeMillis();
                if ((currentTime - beforeSort) > linearSearchTime * 10) {
                    return false;
                }
            }
        }
        return true;
    }

    public SearchResult linearSearchPhoneNumbers(String findFileName) {
        List<String> contactsToFind = getDataFromFile(findFileName);
        long found = contactsToFind.stream()
                .map(String::trim)
                .filter(this::isPresent)
                .count();
        return new SearchResult(contactsToFind.size(), (int) found);
    }

    private boolean isPresent(String fullName) {
        return contacts.stream()
                .anyMatch(it -> fullName.equals(it.getFullName()));
    }

    public SearchResult jumpSearchPhoneNumbers(String findFileName) {
        List<String> contactsToFind = getDataFromFile(findFileName);
        long found = contactsToFind.stream()
                .map(String::trim)
                .filter(this::jumpSearch)
                .count();
        return new SearchResult(contactsToFind.size(), (int) found);
    }

    private boolean jumpSearch(String fullName) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If list is empty, the element is not found */
        if (sortedContacts.size() == 0) {
            return false;
        }

        /* Check the first element */
        if (fullName.equals(sortedContacts.get(currentRight).getFullName())) {
            return true;
        }

        /* Calculating the jump length over list elements */
        int jumpLength = (int) Math.sqrt(sortedContacts.size());

        /* Finding a block where the element may be present */
        while (currentRight < sortedContacts.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(sortedContacts.size() - 1, currentRight + jumpLength);

            if (sortedContacts.get(currentRight).getFullName().compareTo(fullName) >= 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == sortedContacts.size() - 1) && fullName.compareTo(sortedContacts.get(currentRight).getFullName()) > 0) {
            return false;
        }

        /* Doing linear search in the found block */
        return backwardSearch(fullName, prevRight, currentRight);
    }

    private boolean backwardSearch(String fullName, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (fullName.equals(sortedContacts.get(i).getFullName())) {
                return true;
            }
        }
        return false;
    }

    private void loadContacts(String directoryFileName) {
        getDataFromFile(directoryFileName).stream()
                .map(String::trim)
                .forEach(it -> contacts.add(parsePhoneContact(it)));
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

    private List<String> getDataFromFile(String fileName) {
        try {
            URL url = getClass().getClassLoader().getResource(fileName);
            URI uri = Objects.requireNonNull(url).toURI();
            return Files.readAllLines(Path.of(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
