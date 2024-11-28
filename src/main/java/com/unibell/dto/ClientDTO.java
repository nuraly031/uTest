package com.unibell.dto;

import java.util.List;

public class ClientDTO {
    private Long id;
    private String name;
    private List<ContactDTO> contacts;

    public ClientDTO(Long id, String name, List<ContactDTO> contacts) {
        this.id = id;
        this.name = name;
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
