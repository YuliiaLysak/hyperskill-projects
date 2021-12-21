package edu.lysak.contacts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {
    private String name;
    private String surname;
    private String phoneNumber;

    public Contact(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        if (isValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
    }

    public boolean hasNumber() {
        return !phoneNumber.isEmpty();
    }

    private boolean isValid(String phone) {
        Pattern phonePattern = Pattern.compile("(^((\\+?(\\([a-zA-Z0-9]+\\))([- ][a-zA-Z0-9]{2,})*)|(\\+?([a-zA-Z0-9]+)[- ](\\([a-zA-Z0-9]{2,}\\))*)|(\\+?([a-zA-Z0-9]+)[- ]([a-zA-Z0-9]{2,})*))([- ][a-zA-Z0-9]{2,})*$)|(^\\+?[a-zA-Z0-9]+$)");
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }
}
