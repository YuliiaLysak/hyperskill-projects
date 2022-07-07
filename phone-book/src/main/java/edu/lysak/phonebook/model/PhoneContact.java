package edu.lysak.phonebook.model;

public class PhoneContact implements Comparable<PhoneContact> {
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

    @Override
    public int compareTo(PhoneContact other) {
        return this.getFullName().compareTo(other.getFullName());
    }
}
