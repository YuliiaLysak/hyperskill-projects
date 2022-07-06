package edu.lysak.phonebook;

public class Main {
    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        PhoneBookApp phoneBookApp = new PhoneBookApp(phoneBook);
        String directoryFileName = "directory.txt"; // https://stepik.org/media/attachments/lesson/197761/directory.txt
        String findFileName = "find.txt"; // https://stepik.org/media/attachments/lesson/197761/find.txt
        phoneBookApp.run(directoryFileName, findFileName);
    }
}
