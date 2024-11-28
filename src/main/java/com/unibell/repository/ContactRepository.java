package com.unibell.repository;

import com.unibell.model.Client;
import com.unibell.model.Contact;
import com.unibell.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByClient(Client client);
    List<Contact> findByClientAndType(Client client, ContactType type);
}
