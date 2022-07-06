package edu.lysak.phonebook;

public class PhoneContact {
    private String fullName;
    private String phoneNumber;

    public PhoneContact() {
    }

    public PhoneContact(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
