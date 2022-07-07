package edu.lysak.phonebook;

import edu.lysak.phonebook.model.SearchResult;
import edu.lysak.phonebook.util.FileUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PhoneBookApp {
    private final PhoneBook phoneBook;

    public PhoneBookApp(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    public void run(String findFileName) {
        List<String> contactsToFind = FileUtils.getDataFromFile(findFileName);
        long linearSearchTimeDifference = linearSearch(contactsToFind);
        bubbleSortAndJumpSearch(contactsToFind, linearSearchTimeDifference);
        quickSortAndBinarySearch(contactsToFind);
        hashTableSearch(contactsToFind);
    }

    private long linearSearch(List<String> contactsToFind) {
        System.out.println("Start searching (linear search)...");

        long before = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.linearSearchPhoneNumbers(contactsToFind);
        long after = System.currentTimeMillis();

        long takenTime = after - before;
        printTakenTime(takenTime, searchResult);
        return takenTime;
    }

    private void bubbleSortAndJumpSearch(List<String> contactsToFind, long linearSearchTime) {
        System.out.println("\nStart searching (bubble sort + jump search)...");

        long beforeBubbleSort = System.currentTimeMillis();
        boolean sorted = phoneBook.bubbleSortPhoneNumbers(beforeBubbleSort, linearSearchTime);
        long afterBubbleSort = System.currentTimeMillis();

        if (sorted) {
            long beforeJumpSearch = System.currentTimeMillis();
            SearchResult searchResult = phoneBook.jumpSearchPhoneNumbers(contactsToFind);
            long afterJumpSearch = System.currentTimeMillis();

            printTakenTime(afterJumpSearch - beforeBubbleSort, searchResult);
            System.out.printf("Sorting time: %s%n", getTakenTimeString(afterBubbleSort - beforeBubbleSort));
            System.out.printf("Searching time: %s%n", getTakenTimeString(afterJumpSearch - beforeJumpSearch));
            return;
        }

        long beforeLinearSearch = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.linearSearchPhoneNumbers(contactsToFind);
        long afterLinearSearch = System.currentTimeMillis();

        printTakenTime(afterLinearSearch - beforeBubbleSort, searchResult);
        System.out.printf("Sorting time: %s - STOPPED, moved to linear search%n", getTakenTimeString(afterBubbleSort - beforeBubbleSort));
        System.out.printf("Searching time: %s%n", getTakenTimeString(afterLinearSearch - beforeLinearSearch));
    }

    private void quickSortAndBinarySearch(List<String> contactsToFind) {
        System.out.println("\nStart searching (quick sort + binary search)...");

        long beforeQuickSort = System.currentTimeMillis();
        phoneBook.quickSortPhoneNumbers();
        long afterQuickSort = System.currentTimeMillis();

        long beforeBinarySearch = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.binarySearchPhoneNumbers(contactsToFind);
        long afterBinarySearch = System.currentTimeMillis();

        printTakenTime(afterBinarySearch - beforeQuickSort, searchResult);
        System.out.printf("Sorting time: %s%n", getTakenTimeString(afterQuickSort - beforeQuickSort));
        System.out.printf("Searching time: %s%n", getTakenTimeString(afterBinarySearch - beforeBinarySearch));
    }

    private void hashTableSearch(List<String> contactsToFind) {
        System.out.println("\nStart searching (hash table)...");

        long beforeHashTableCreation = System.currentTimeMillis();
        phoneBook.createAndFillHashTable();
        long afterHashTableCreation = System.currentTimeMillis();

        long beforeSearchInHashTable = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.hashTableSearch(contactsToFind);
        long afterSearchInHashTable = System.currentTimeMillis();

        printTakenTime(afterSearchInHashTable - beforeHashTableCreation, searchResult);
        System.out.printf("Creating time: %s%n", getTakenTimeString(afterHashTableCreation - beforeHashTableCreation));
        System.out.printf("Searching time: %s%n", getTakenTimeString(afterSearchInHashTable - beforeSearchInHashTable));
    }

    private void printTakenTime(long takenTime, SearchResult searchResult) {
        System.out.printf("Found %s / %s entries. Time taken: %s%n",
                searchResult.getFound(),
                searchResult.getAll(),
                getTakenTimeString(takenTime)
        );
    }

    private String getTakenTimeString(long takenTime) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(takenTime);
        takenTime -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(takenTime);
        long milliSeconds = takenTime - TimeUnit.SECONDS.toMillis(seconds);
        return String.format("%s min. %s sec. %s ms.", minutes, seconds, milliSeconds);
    }
}
