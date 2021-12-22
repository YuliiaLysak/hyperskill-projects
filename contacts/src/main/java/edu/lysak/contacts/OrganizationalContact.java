package edu.lysak.contacts;

import java.time.LocalDateTime;

public class OrganizationalContact extends Contact {
    private String name;
    private String address;

    public OrganizationalContact() {
        this.setPerson(false);
        this.setCreated(LocalDateTime.now());
        this.setEdited(LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
