package edu.lysak.phonebook.service;

import edu.lysak.phonebook.model.PhoneContact;
import edu.lysak.phonebook.model.SearchResult;

import java.util.Hashtable;
import java.util.List;

public class SearchingService {

    public SearchResult linearSearchPhoneNumbers(List<PhoneContact> contacts, List<String> contactsToFind) {
        long found = contactsToFind.stream()
                .map(String::trim)
                .filter(fullName -> isPresent(contacts, fullName))
                .count();
        return new SearchResult(contactsToFind.size(), (int) found);
    }

    private boolean isPresent(List<PhoneContact> contacts, String fullName) {
        return contacts.stream()
                .anyMatch(it -> fullName.equals(it.getFullName()));
    }

    public SearchResult jumpSearchPhoneNumbers(List<PhoneContact> bubbleSortedContacts, List<String> contactsToFind) {
        long found = contactsToFind.stream()
                .map(String::trim)
                .filter(fullName -> jumpSearch(bubbleSortedContacts, fullName))
                .count();
        return new SearchResult(contactsToFind.size(), (int) found);
    }

    private boolean jumpSearch(List<PhoneContact> bubbleSortedContacts, String fullName) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If list is empty, the element is not found */
        if (bubbleSortedContacts.size() == 0) {
            return false;
        }

        /* Check the first element */
        if (fullName.equals(bubbleSortedContacts.get(currentRight).getFullName())) {
            return true;
        }

        /* Calculating the jump length over list elements */
        int jumpLength = (int) Math.sqrt(bubbleSortedContacts.size());

        /* Finding a block where the element may be present */
        while (currentRight < bubbleSortedContacts.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(bubbleSortedContacts.size() - 1, currentRight + jumpLength);

            if (bubbleSortedContacts.get(currentRight).getFullName().compareTo(fullName) >= 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == bubbleSortedContacts.size() - 1) && fullName.compareTo(bubbleSortedContacts.get(currentRight).getFullName()) > 0) {
            return false;
        }

        /* Doing linear search in the found block */
        return backwardSearch(bubbleSortedContacts, fullName, prevRight, currentRight);
    }

    private boolean backwardSearch(List<PhoneContact> bubbleSortedContacts, String fullName, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (fullName.equals(bubbleSortedContacts.get(i).getFullName())) {
                return true;
            }
        }
        return false;
    }

    public SearchResult binarySearchPhoneNumbers(List<PhoneContact> quickSortedContacts, List<String> contactsToFind) {
        long found = contactsToFind.stream()
                .map(String::trim)
                .filter(fullName -> binarySearchIterative(quickSortedContacts, fullName))
                .count();
        return new SearchResult(contactsToFind.size(), (int) found);

//        long found = contactsToFind.stream()
//                .map(String::trim)
//                .filter(fullName -> binarySearchRecursive(quickSortedContacts, fullName, 0, quickSortedContacts.size() - 1))
//                .count();
//        return new SearchResult(contactsToFind.size(), (int) found);
    }

    private boolean binarySearchIterative(List<PhoneContact> quickSortedContacts, String fullName) {
        int left = 0;
        int right = quickSortedContacts.size() - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            if (fullName.equals(quickSortedContacts.get(middle).getFullName())) {
                return true;
            } else if (quickSortedContacts.get(middle).getFullName().compareTo(fullName) < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return false;
    }

    private boolean binarySearchRecursive(List<PhoneContact> quickSortedContacts, String fullName, int left, int right) {
        if (left > right) {
            return false; // search interval is empty, the element is not found
        }

        int mid = left + (right - left) / 2; // the index of the middle element -> or (left + right) >>> 1  or (left + right) / 2

        if (fullName.equals(quickSortedContacts.get(mid).getFullName())) {
            return true; // the element is found
        } else if (quickSortedContacts.get(mid).getFullName().compareTo(fullName) > 0) {
            return binarySearchRecursive(quickSortedContacts, fullName, left, mid - 1); // go to the left subarray
        } else {
            return binarySearchRecursive(quickSortedContacts, fullName, mid + 1, right); // go to the right subarray
        }
    }

    public SearchResult hashTableSearch(Hashtable<String, PhoneContact> contactsHashTable, List<String> contactsToFind) {
        int found = 0;
        for (String fullName : contactsToFind) {
            if (contactsHashTable.containsKey(fullName)) {
                found++;
            }
        }
        return new SearchResult(contactsToFind.size(), found);
    }
}
