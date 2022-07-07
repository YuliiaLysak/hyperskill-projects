package edu.lysak.phonebook;

public class Main {
    public static void main(String[] args) {
        String directoryFileName = "directory.txt"; // https://stepik.org/media/attachments/lesson/197761/directory.txt
        String findFileName = "find.txt"; // https://stepik.org/media/attachments/lesson/197761/find.txt
        PhoneBook phoneBook = new PhoneBook(directoryFileName);
        PhoneBookApp phoneBookApp = new PhoneBookApp(phoneBook);
        phoneBookApp.run(findFileName);
    }
}
