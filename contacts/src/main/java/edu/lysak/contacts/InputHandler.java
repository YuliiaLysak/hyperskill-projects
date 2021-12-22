package edu.lysak.contacts;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final PhoneBook phoneBook;

    public InputHandler(Scanner scanner, PhoneBook phoneBook) {
        this.scanner = scanner;
        this.phoneBook = phoneBook;
    }

    public void proceed() throws IOException {
        while (true) {
            System.out.println("\n[menu] Enter action (add, list, search, count, exit):");
            String action = scanner.nextLine();
            switch (action) {
                case "add":
                    processAddAction();
                    phoneBook.updateFile();
                    break;
                case "list":
                    processListAction();
                    break;
                case "search":
                    processSearchAction();
                    break;
                case "count":
                    processCountAction();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println(("Action [" + action + "] is not supported"));
            }
        }
    }

    private void processAddAction() {
        System.out.println("Enter the type (person, organization):");
        String contactType = scanner.nextLine();
        switch (contactType) {
            case "person" -> {
                System.out.println("Enter the name:");
                String name = scanner.nextLine();
                System.out.println("Enter the surname:");
                String surname = scanner.nextLine();
                System.out.println("Enter the birth date:");
                String birthDate = scanner.nextLine();
                checkBirthDate(birthDate);
                System.out.println("Enter the gender (M, F):");
                String gender = scanner.nextLine();
                checkGender(gender);
                System.out.println("Enter the number:");
                String personNumber = scanner.nextLine();
                phoneBook.addPerson(name, surname, birthDate, gender, personNumber);
            }
            case "organization" -> {
                System.out.println("Enter the organization name:");
                String organizationName = scanner.nextLine();
                System.out.println("Enter the address:");
                String address = scanner.nextLine();
                System.out.println("Enter the number:");
                String organizationNumber = scanner.nextLine();
                phoneBook.addOrganization(organizationName, address, organizationNumber);
            }
            default -> {
                System.out.println(("Contact type [" + contactType + "] is not supported"));
                return;
            }
        }

        System.out.println("The record added.");
    }

    private void processListAction() {
        List<Contact> all = phoneBook.getAll();

        if (all.isEmpty()) {
            System.out.println("The Phone Book is empty.");
            return;
        }

        showContactsNames(all);

        System.out.println("\n[list] Enter action ([number], back):");
        String listAction = scanner.nextLine();
        try {
            int index = Integer.parseInt(listAction);
            Contact contact = all.get(index - 1);
            processRecordActions(contact);
        } catch (NumberFormatException | IOException exception) {
            if (!"back".equals(listAction)) {
                System.out.println(("List action [" + listAction + "] is not supported"));
            }
            return;
        }
    }

    private void processSearchAction() {
        List<Contact> all = phoneBook.getAll();

        if (all.isEmpty()) {
            System.out.println("The Phone Book is empty.");
            return;
        }

        while (true) {
            System.out.println("Enter search query:");
            String query = scanner.nextLine();
            List<Contact> searchResult = phoneBook.getSearchResult(query);
            if (searchResult.size() == 1) {
                System.out.println("Found 1 result:");
            } else {
                System.out.printf("Found %d results:%n", searchResult.size());
            }
            showContactsNames(searchResult);

            System.out.println("\n[search] Enter action ([number], back, again):");
            String searchAction = scanner.nextLine();
            try {
                int number = Integer.parseInt(searchAction);
                processRecordActions(searchResult.get(number - 1));
                return;
            } catch (NumberFormatException | IOException exception) {
                switch (searchAction) {
                    case "again":
                        continue;
                    case "back":
                        return;
                    default:
                        System.out.println(("Search action [" + searchAction + "] is not supported"));
                }
            }
        }
    }

    private void processCountAction() {
        System.out.printf("The Phone Book has %s records.%n", phoneBook.count());
    }

    private void processRecordActions(Contact contact) throws IOException {
        showContactInfo(contact);
        while (true) {
            System.out.println("\n[record] Enter action (edit, delete, menu):");
            String recordAction = scanner.nextLine();
            switch (recordAction) {
                case "edit":
                    processEditAction(contact);
                    phoneBook.updateFile();
                    break;
                case "delete":
                    processDeleteAction(contact);
                    phoneBook.updateFile();
                    break;
                case "menu":
                    return;
                default:
                    System.out.println(("Record action [" + recordAction + "] is not supported"));
            }
        }
    }

    private void processEditAction(Contact contact) {
        System.out.printf("Select a field (%s, number):%n", contact.getAllFieldsAsString());
        String field = scanner.nextLine();
        System.out.printf("Enter %s:%n", field);
        String value = scanner.nextLine();

        contact.editField(field, value);
        System.out.println("Saved");
        showContactInfo(contact);
    }

    private void processDeleteAction(Contact contact) {
        phoneBook.remove(contact);
        System.out.println("The record removed!");
    }

    private void showContactsNames(List<Contact> contacts) {
        contacts.forEach(contact -> System.out.printf(
                "%d. %s%n",
                contacts.indexOf(contact) + 1,
                contact.getContactName())
        );
    }

    private void showContactInfo(Contact contact) {
        Map<String, String> fields = contact.getAllFieldsAsMap();
        fields.forEach((key, value) -> System.out.printf(
                "%s: %s%n",
                value,
                contact.getFieldValue(key)));
        String number = contact.hasNumber() ? contact.getPhoneNumber() : "[no number]";
        System.out.println("Number: " + number);
        System.out.println("Time created: " + contact.getCreated());
        System.out.println("Time last edit: " + contact.getEdited());
    }

    private void checkBirthDate(String birthDate) {
        if (birthDate.isEmpty()) {
            System.out.println("Bad birth date!");
        }
    }

    private void checkGender(String gender) {
        if (gender.isEmpty()) {
            System.out.println("Bad gender!");
        }
    }
}
