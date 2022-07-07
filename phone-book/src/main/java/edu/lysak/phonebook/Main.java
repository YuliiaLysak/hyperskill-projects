package edu.lysak.phonebook;

import edu.lysak.phonebook.service.SearchingService;
import edu.lysak.phonebook.service.SortingService;

public class Main {
    public static void main(String[] args) {
        String directoryFileName = "directory.txt"; // https://stepik.org/media/attachments/lesson/197761/directory.txt
        String findFileName = "find.txt"; // https://stepik.org/media/attachments/lesson/197761/find.txt
        SortingService sortingService = new SortingService();
        SearchingService searchingService = new SearchingService();
        PhoneBook phoneBook = new PhoneBook(directoryFileName, sortingService, searchingService);
        PhoneBookApp phoneBookApp = new PhoneBookApp(phoneBook);
        phoneBookApp.run(findFileName);
    }
}
