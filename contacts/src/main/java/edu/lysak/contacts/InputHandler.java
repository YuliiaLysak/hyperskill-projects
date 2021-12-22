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
            System.out.println("Enter action (add, remove, edit, count, info, exit):");
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
                case "info":
                    processInfoAction();
                    break;
                case "exit":
                    return;
            }
            System.out.println();
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
                String gender = phoneBook.getGender(scanner.nextLine());
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
        }

        System.out.println("The record added.");
    }

    private void checkBirthDate(String birthDate) {
        if (birthDate.isEmpty()) {
            System.out.println("Bad birth date!");
        }
    }

    private void processRemoveAction() {
        if (phoneBook.count() == 0) {
            System.out.println("No records to remove!");
        } else {
            showContactsNames();
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
            showContactsNames();
            System.out.println("Select a record:");
            int index = Integer.parseInt(scanner.nextLine());
            if (phoneBook.isPerson(index - 1)) {
                System.out.println("Select a field (name, surname, birth, gender, number):");
                String field = scanner.nextLine();
                System.out.printf("Enter %s:%n", field);
                String value = scanner.nextLine();

                phoneBook.editPerson(index - 1, field, value);

            } else {
                System.out.println("Select a field (name, address, number):");
                String field = scanner.nextLine();
                System.out.printf("Enter %s:%n", field);
                String value = scanner.nextLine();

                phoneBook.editOrganization(index - 1, field, value);
            }
            System.out.println("The record updated!");
        }
    }

    private void processCountAction() {
        System.out.printf("The Phone Book has %s records.%n", phoneBook.count());
    }

    private void processInfoAction() {
        showContactsNames();
        System.out.println("Enter index to show info:");
        int index = Integer.parseInt(scanner.nextLine());

        List<Contact> contacts = phoneBook.getAll();
        Contact contact = contacts.get(index - 1);
        if (contact.isPerson()) {
            showPersonInfo((PersonalContact) contact);
        } else {
            showOrgInfo((OrganizationalContact) contact);
        }

        String number = contact.hasNumber() ? contact.getPhoneNumber() : "[no number]";
        System.out.println("Number: " + number);
        System.out.println("Time created: " + contact.getCreated());
        System.out.println("Time last edit: " + contact.getEdited());
    }

    private void showContactsNames() {
        List<Contact> contacts = phoneBook.getAll();
        for (Contact contact : contacts) {
            if (contact.isPerson()) {
                PersonalContact person = (PersonalContact) contact;
                System.out.printf("%d. %s %s%n",
                        contacts.indexOf(person) + 1,
                        person.getName(),
                        person.getSurname()
                );
            } else {
                OrganizationalContact org = (OrganizationalContact) contact;
                System.out.printf("%d. %s%n",
                        contacts.indexOf(org) + 1,
                        org.getName()
                );
            }
        }
    }

    private void showPersonInfo(PersonalContact person) {
        System.out.println("Name: " + person.getName());
        System.out.println("Surname: " + person.getSurname());
        String birthDate = person.getBirthDate() != null ?
                person.getBirthDate().toString() :
                "[no data]";
        String gender = person.getGender().isEmpty() ? "[no data]" : person.getGender();
        System.out.println("Birth date: " + birthDate);
        System.out.println("Gender: " + gender);
    }

    private void showOrgInfo(OrganizationalContact org) {
        System.out.println("Organization name: " + org.getName());
        System.out.println("Address: " + org.getAddress());
    }
}
