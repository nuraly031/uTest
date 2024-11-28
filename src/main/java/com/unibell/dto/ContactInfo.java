package com.unibell.dto;

import com.unibell.model.ContactType;

import java.util.List;

public class ContactInfo {
    private List<String> phones;
    private List<String> emails;

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "phones=" + phones +
                ", emails=" + emails +
                '}';
    }
}