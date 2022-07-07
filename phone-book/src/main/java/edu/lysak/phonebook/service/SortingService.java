package edu.lysak.phonebook.service;

import edu.lysak.phonebook.model.PhoneContact;

import java.util.List;

public class SortingService {

    public boolean bubbleSortPhoneNumbers(List<PhoneContact> bubbleSortedContacts,
                                          long beforeSort,
                                          long linearSearchTime) {

        for (int i = 0; i < bubbleSortedContacts.size(); i++) {
            for (int j = 1; j < (bubbleSortedContacts.size() - i); j++) {
                if (bubbleSortedContacts.get(j - 1).compareTo(bubbleSortedContacts.get(j)) > 0) {
                    PhoneContact temp = bubbleSortedContacts.get(j - 1);
                    bubbleSortedContacts.set(j - 1, bubbleSortedContacts.get(j));
                    bubbleSortedContacts.set(j, temp);
                }
                long currentTime = System.currentTimeMillis();
                if (currentTime - beforeSort > linearSearchTime * 10) {
                    return false;
                }
            }
        }
        return true;
    }

    public void quickSortPhoneNumbers(List<PhoneContact> quickSortedContacts, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(quickSortedContacts, begin, end);
            quickSortPhoneNumbers(quickSortedContacts, begin, partitionIndex - 1);
            quickSortPhoneNumbers(quickSortedContacts, partitionIndex + 1, end);
        }
    }

    private int partition(List<PhoneContact> quickSortedContacts, int begin, int end) {
        PhoneContact pivot = quickSortedContacts.get(end);
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (quickSortedContacts.get(j).compareTo(pivot) <= 0) {
                i++;
                PhoneContact swapTemp = quickSortedContacts.get(i);
                quickSortedContacts.set(i, quickSortedContacts.get(j));
                quickSortedContacts.set(j, swapTemp);
            }
        }

        PhoneContact swapTemp = quickSortedContacts.get(i + 1);
        quickSortedContacts.set(i + 1, quickSortedContacts.get(end));
        quickSortedContacts.set(end, swapTemp);

        return i + 1;
    }
}
