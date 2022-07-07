package edu.lysak.phonebook;

import edu.lysak.phonebook.model.PhoneContact;
import edu.lysak.phonebook.model.SearchResult;
import edu.lysak.phonebook.service.SearchingService;
import edu.lysak.phonebook.service.SortingService;
import edu.lysak.phonebook.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {
    private final List<PhoneContact> contacts = new ArrayList<>();
    private List<PhoneContact> bubbleSortedContacts;
    private List<PhoneContact> quickSortedContacts;
    private final SortingService sortingService;
    private final SearchingService searchingService;

    public PhoneBook(String directoryFileName,
                     SortingService sortingService,
                     SearchingService searchingService) {
        this.sortingService = sortingService;
        this.searchingService = searchingService;
        loadContacts(directoryFileName);
    }

    public boolean bubbleSortPhoneNumbers(long beforeBubbleSort, long linearSearchTime) {
        bubbleSortedContacts = new ArrayList<>(List.copyOf(contacts));
        return sortingService.bubbleSortPhoneNumbers(bubbleSortedContacts, beforeBubbleSort, linearSearchTime);
    }

    public void quickSortPhoneNumbers() {
        quickSortedContacts = new ArrayList<>(List.copyOf(contacts));
        sortingService.quickSortPhoneNumbers(quickSortedContacts, 0, quickSortedContacts.size() - 1);
    }

    public SearchResult linearSearchPhoneNumbers(List<String> contactsToFind) {
        return searchingService.linearSearchPhoneNumbers(contacts, contactsToFind);
    }

    public SearchResult jumpSearchPhoneNumbers(List<String> contactsToFind) {
        return searchingService.jumpSearchPhoneNumbers(bubbleSortedContacts, contactsToFind);
    }

    public SearchResult binarySearchPhoneNumbers(List<String> contactsToFind) {
        return searchingService.binarySearchPhoneNumbers(quickSortedContacts, contactsToFind);
    }

    private void loadContacts(String directoryFileName) {
        FileUtils.getDataFromFile(directoryFileName).stream()
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
}
