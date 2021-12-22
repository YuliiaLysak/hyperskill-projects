package edu.lysak.contacts;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact {
    private String phoneNumber;
    private boolean isPerson;
    private LocalDateTime created;
    private LocalDateTime edited;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isValidPnoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
    }

    public boolean hasNumber() {
        return !phoneNumber.isEmpty();
    }

    public boolean isPerson() {
        return isPerson;
    }

    public void setPerson(boolean person) {
        isPerson = person;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }

    private boolean isValidPnoneNumber(String phone) {
        Pattern phonePattern = Pattern.compile("(^((\\+?(\\([a-zA-Z0-9]+\\))([- ][a-zA-Z0-9]{2,})*)|(\\+?([a-zA-Z0-9]+)[- ](\\([a-zA-Z0-9]{2,}\\))*)|(\\+?([a-zA-Z0-9]+)[- ]([a-zA-Z0-9]{2,})*))([- ][a-zA-Z0-9]{2,})*$)|(^\\+?[a-zA-Z0-9]+$)");
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }
}
