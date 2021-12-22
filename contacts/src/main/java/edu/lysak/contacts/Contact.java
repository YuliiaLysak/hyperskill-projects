package edu.lysak.contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact implements Serializable {
    private String phoneNumber;
    private final LocalDateTime created = LocalDateTime.now();
    private LocalDateTime edited = LocalDateTime.now();

    public abstract String getAllFieldsAsString();

    public abstract Map<String, String> getAllFieldsAsMap();

    public abstract void editField(String fieldName, String newValue);

    public abstract String getFieldValue(String fieldName);

    public abstract String getContactName();

    public abstract String getConcatenatedFields();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Wrong number format!");
            this.phoneNumber = "";
        }
    }

    public boolean hasNumber() {
        return !phoneNumber.isEmpty();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public void setEdited(LocalDateTime edited) {
        this.edited = edited;
    }

    private boolean isValidPhoneNumber(String phone) {
        Pattern phonePattern = Pattern.compile("(^((\\+?(\\([a-zA-Z0-9]+\\))([- ][a-zA-Z0-9]{2,})*)|(\\+?([a-zA-Z0-9]+)[- ](\\([a-zA-Z0-9]{2,}\\))*)|(\\+?([a-zA-Z0-9]+)[- ]([a-zA-Z0-9]{2,})*))([- ][a-zA-Z0-9]{2,})*$)|(^\\+?[a-zA-Z0-9]+$)");
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }
}
