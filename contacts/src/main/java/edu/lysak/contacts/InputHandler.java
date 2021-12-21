package edu.lysak.contacts;

import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final PhoneBook phoneBook;

    public InputHandler(Scanner scanner, PhoneBook phoneBook) {
        this.scanner = scanner;
        this.phoneBook = phoneBook;
    }

    public void proceed() {
        while (true) {
            System.out.println("Enter action (add, remove, edit, count, list, exit):");
            String action = scanner.nextLine();
            switch (action) {
                case "add":
                    processAddAction();
                    break;
                case "remove":
                    processRemoveAction();
                    break;
                case "edit":
                    processEditAction();
                    break;
                case "count":
                    processCountAction();
                    break;
                case "list":
                    processListAction();
                    break;
                case "exit":
                    return;
            }
        }
    }

    private void processAddAction() {
        System.out.println("Enter the name:");
        String name = scanner.nextLine();
        System.out.println("Enter the surname:");
        String surname = scanner.nextLine();
        System.out.println("Enter the number:");
        String phone = scanner.nextLine();

        phoneBook.add(name, surname, phone);
        System.out.println("The record added.");
    }

    private void processRemoveAction() {
        if (phoneBook.count() == 0) {
            System.out.println("No records to remove!");
        } else {
            processListAction();
            System.out.println("Select a record:");
            int index = Integer.parseInt(scanner.nextLine());

            phoneBook.remove(index - 1);
            System.out.println("The record removed!");
        }
    }

    private void processEditAction() {
        if (phoneBook.count() == 0) {
            System.out.println("No records to edit!");
        } else {
            processListAction();
            System.out.println("Select a record:");
            int index = Integer.parseInt(scanner.nextLine());
            System.out.println("Select a field (name, surname, number):");
            String field = scanner.nextLine();
            System.out.printf("Enter %s:%n", field);
            String value = scanner.nextLine();

            phoneBook.edit(index - 1, field, value);
            System.out.println("The record updated!");
        }
    }

    private void processCountAction() {
        System.out.printf("The Phone Book has %s records.%n", phoneBook.count());
    }

    private void processListAction() {
        List<Contact> contacts = phoneBook.getAll();
        contacts.forEach(c -> System.out.printf(
                "%d. %s %s, %s%n",
                contacts.indexOf(c) + 1,
                c.getName(),
                c.getSurname(),
                c.hasNumber() ? c.getPhoneNumber() : "[no number]")
        );
    }
}
