package com.unibell.config;

import com.unibell.model.Client;
import com.unibell.model.Contact;
import com.unibell.model.ContactType;
import com.unibell.repository.ClientRepository;
import com.unibell.repository.ContactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AddTestData implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    public AddTestData(ClientRepository clientRepository, ContactRepository contactRepository) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Client client1 = new Client();
        client1.setName("Шарапиев Нурали");
        client1.setContacts(new ArrayList<>());

        Contact contact1 = new Contact();
        contact1.setClient(client1);
        contact1.setType(ContactType.PHONE);
        contact1.setValue("777-222-0110");

        Contact contact2 = new Contact();
        contact2.setClient(client1);
        contact2.setType(ContactType.EMAIL);
        contact2.setValue("test@g.com");

        clientRepository.save(client1);
        contactRepository.save(contact1);
        contactRepository.save(contact2);
//        --------------------------------------
        Client client2 = new Client();
        client2.setName("Райц Наталья");
        client2.setContacts(new ArrayList<>());

        Contact contact3 = new Contact();
        contact3.setClient(client2);
        contact3.setType(ContactType.PHONE);
        contact3.setValue("777-222-0010");

        Contact contact4 = new Contact();
        contact4.setClient(client2);
        contact4.setType(ContactType.EMAIL);
        contact4.setValue("test1@g.com");

        clientRepository.save(client2);
        contactRepository.save(contact3);
        contactRepository.save(contact4);

        System.out.println("Test data Loading)");
    }
}
