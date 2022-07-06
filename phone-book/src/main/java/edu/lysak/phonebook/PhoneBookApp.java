package edu.lysak.phonebook;

import java.util.concurrent.TimeUnit;

public class PhoneBookApp {
    private final PhoneBook phoneBook;

    public PhoneBookApp(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    public void run(String directoryFileName, String findFileName) {
        phoneBook.loadContacts(directoryFileName);
        System.out.println("Start searching...");

        long before = System.currentTimeMillis();
        SearchResult searchResult = phoneBook.searchPhoneNumbers(findFileName);
        long after = System.currentTimeMillis();

        System.out.printf("Found %s / %s entries. Time taken: %s%n",
                searchResult.getFound(),
                searchResult.getAll(),
                getTakenTime(before, after)
        );
    }

    private String getTakenTime(long before, long after) {
        long timeDifference = after - before;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        timeDifference -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
        long milliSeconds = timeDifference - TimeUnit.SECONDS.toMillis(seconds);
        return String.format("%s min. %s sec. %s ms.", minutes, seconds, milliSeconds);
    }
}
