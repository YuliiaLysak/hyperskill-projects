package edu.lysak.phonebook;

import java.util.concurrent.TimeUnit;

public class PhoneBookApp {
    private final PhoneBook phoneBook;

    public PhoneBookApp(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    public void run(String findFileName) {
        long linearSearchTimeDifference = linearSearch(findFileName);
        bubbleSortAndJumpSearch(findFileName, linearSearchTimeDifference);
    }

    private long linearSearch(String findFileName) {
        System.out.println("Start searching (linear search)...");

        long before = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.linearSearchPhoneNumbers(findFileName);
        long after = System.currentTimeMillis();

        long takenTime = after - before;
        printTakenTime(takenTime, searchResult);
        return takenTime;
    }

    private void bubbleSortAndJumpSearch(String findFileName, long linearSearchTime) {
        System.out.println("\nStart searching (bubble sort + jump search)...");

        long beforeBubbleSort = System.currentTimeMillis();
        boolean sorted = phoneBook.bubbleSortPhoneNumbers(beforeBubbleSort, linearSearchTime);
        long afterBubbleSort = System.currentTimeMillis();

        if (sorted) {
            long beforeJumpSearch = System.currentTimeMillis();
            SearchResult searchResult = phoneBook.jumpSearchPhoneNumbers(findFileName);
            long afterJumpSearch = System.currentTimeMillis();

            printTakenTime(afterJumpSearch - beforeBubbleSort, searchResult);
            System.out.printf("Sorting time: %s%n", getTakenTimeString(afterBubbleSort - beforeBubbleSort));
            System.out.printf("Searching time: %s%n", getTakenTimeString(afterJumpSearch - beforeJumpSearch));
            return;
        }

        long beforeLinearSearch = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.linearSearchPhoneNumbers(findFileName);
        long afterLinearSearch = System.currentTimeMillis();

        printTakenTime(afterLinearSearch - beforeBubbleSort, searchResult);
        System.out.printf("Sorting time: %s - STOPPED, moved to linear search%n", getTakenTimeString(afterBubbleSort - beforeBubbleSort));
        System.out.printf("Searching time: %s%n", getTakenTimeString(afterLinearSearch - beforeLinearSearch));
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
