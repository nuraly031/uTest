package com.unibell.service;

import com.unibell.dto.ClientDTO;
import com.unibell.dto.ContactDTO;
import com.unibell.dto.ContactInfo;
import com.unibell.exception.ClientNotFoundException;
import com.unibell.model.Client;
import com.unibell.model.Contact;
import com.unibell.model.ContactType;
import com.unibell.repository.ClientRepository;
import com.unibell.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    public ClientService(ClientRepository clientRepository, ContactRepository contactRepository) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
    }

    @Transactional
    public ClientDTO addClient(String name) {
        Client client = new Client();
        client.setName(name);
        client.setContacts(new ArrayList<>());
        return toClientDTO(clientRepository.save(client));
    }

    @Transactional
    public void addContacts(Long clientId, ContactInfo contactsInfo) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с ID " + clientId + " не найден"));

        if (contactsInfo.getPhones() != null) {
            for (String phone : contactsInfo.getPhones()) {
                Contact contact = new Contact();
                contact.setClient(client);
                contact.setType(ContactType.PHONE);
                contact.setValue(phone);
                contactRepository.save(contact);
            }
        }

        if (contactsInfo.getEmails() != null) {
            for (String email : contactsInfo.getEmails()) {
                Contact contact = new Contact();
                contact.setClient(client);
                contact.setType(ContactType.EMAIL);
                contact.setValue(email);
                contactRepository.save(contact);
            }
        }
    }


    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream()
                .map(client -> toClientDTO(client))
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id)
                .map(client -> toClientDTO(client))
                .orElseThrow(() -> new ClientNotFoundException("Клиент с ID " + id + " не найден"));
    }

    public List<ContactDTO> getContactsByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с ID " + clientId + " не найден"));
        return client.getContacts().stream()
                .map(contact -> toContactDTO(contact))
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsByType(Long clientId, ContactType type) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с ID " + clientId + " не найден"));
        return contactRepository.findByClientAndType(client, type).stream()
                .map(contact -> toContactDTO(contact))
                .collect(Collectors.toList());
    }

    private ClientDTO toClientDTO(Client client) {
        List<ContactDTO> contactDTOs = client.getContacts() == null ? new ArrayList<>() :
                client.getContacts().stream()
                        .map(contact -> toContactDTO(contact))
                        .collect(Collectors.toList());
        return new ClientDTO(client.getId(), client.getName(), contactDTOs);
    }

    private ContactDTO toContactDTO(Contact contact) {
        return new ContactDTO(contact.getId(), contact.getType().toString(), contact.getValue());
    }
}
