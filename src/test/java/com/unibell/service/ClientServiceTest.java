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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddClient() {

        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO clientDTO = clientService.addClient("Шарапиев Нурали");

        assertNotNull(clientDTO);
        assertEquals("Шарапиев Нурали", clientDTO.getName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testAddSingleContact() {

        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");
        client.setContacts(new ArrayList<>());

        Contact contact = new Contact();
        contact.setId(1L);
        contact.setType(ContactType.PHONE);
        contact.setValue("777-222-0110");
        contact.setClient(client);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        ContactInfo contactsInfo = new ContactInfo();
        contactsInfo.setPhones(List.of("777-222-0110"));
        contactsInfo.setEmails(new ArrayList<>());

        clientService.addContacts(1L, contactsInfo);

        verify(clientRepository, times(1)).findById(1L);

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    public void testAddContactClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ContactInfo contactsInfo = new ContactInfo();
        contactsInfo.setPhones(List.of("777-222-0110"));
        contactsInfo.setEmails(new ArrayList<>());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.addContacts(1L, contactsInfo);
        });

        verify(clientRepository, times(1)).findById(1L);

        verify(contactRepository, times(0)).save(any(Contact.class));
    }


    @Test
    public void testGetClients() {

        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Шарапиев Нурали");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("Райц Наталья");

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDTO> clientDTOs = clientService.getClients();

        assertNotNull(clientDTOs);
        assertEquals(2, clientDTOs.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testGetClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO clientDTO = clientService.getClientById(1L);

        assertNotNull(clientDTO);
        assertEquals("Шарапиев Нурали", clientDTO.getName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetClientByIdClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClientById(1L);
        });

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetContactsByClientId() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");
        List<Contact> contacts = new ArrayList<>();

        Contact contact1 = new Contact();
        contact1.setId(1L);
        contact1.setType(ContactType.PHONE);
        contact1.setValue("777-222-0110");

        Contact contact2 = new Contact();
        contact2.setId(2L);
        contact2.setType(ContactType.EMAIL);
        contact2.setValue("test@g.com");

        contacts.add(contact1);
        contacts.add(contact2);

        client.setContacts(contacts);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        List<ContactDTO> contactDTOs = clientService.getContactsByClientId(1L);

        assertNotNull(contactDTOs);
        assertEquals(2, contactDTOs.size());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetContactsByClientIdClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.getContactsByClientId(1L);
        });
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetContactsByType() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");

        List<Contact> contacts = new ArrayList<>();
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setType(ContactType.PHONE);
        contact.setValue("222-777-0110");
        contacts.add(contact);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(contactRepository.findByClientAndType(client, ContactType.PHONE)).thenReturn(contacts);

        List<ContactDTO> contactDTOs = clientService.getContactsByType(1L, ContactType.PHONE);

        assertNotNull(contactDTOs);
        assertEquals(1, contactDTOs.size());
        verify(clientRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).findByClientAndType(client, ContactType.PHONE);
    }

    @Test
    public void testGetContactsByTypeClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.getContactsByType(1L, ContactType.PHONE);
        });

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddMultipleContacts() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Шарапиев Нурали");
        client.setContacts(new ArrayList<>());

        List<String> phones = List.of("777-222-0110", "098-765-4321");
        List<String> emails = List.of("test@g.com", "test1@g.com");

        ContactInfo contactsInfo = new ContactInfo();
        contactsInfo.setPhones(phones);
        contactsInfo.setEmails(emails);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        clientService.addContacts(1L, contactsInfo);

        verify(clientRepository, times(1)).findById(1L);

        verify(contactRepository, times(4)).save(any(Contact.class));
    }

    @Test
    public void testAddMultipleContactsClientNotFound() {
        List<String> phones = List.of("777-222-0110", "777-222-0110");
        List<String> emails = List.of("test@g.com", "test1@g.com");

        ContactInfo contactsInfo = new ContactInfo();
        contactsInfo.setPhones(phones);
        contactsInfo.setEmails(emails);

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.addContacts(1L, contactsInfo);
        });

        verify(clientRepository, times(1)).findById(1L);
        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}
