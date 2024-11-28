package com.unibell.controller;

import com.unibell.dto.ClientDTO;
import com.unibell.dto.ContactDTO;
import com.unibell.dto.ContactInfo;
import com.unibell.model.ContactType;
import com.unibell.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDTO> addClient(@RequestBody String name) {
        return ResponseEntity.ok(clientService.addClient(name));
    }

    @PostMapping("/{clientId}/contacts")
    public ResponseEntity<String> addContacts(
            @PathVariable Long clientId,
            @RequestBody ContactInfo contactsInfo) {

        System.out.println("Полученные данные: " + contactsInfo);
        clientService.addContacts(clientId, contactsInfo);
        System.out.println("Контакты добавлены");
        return ResponseEntity.ok("Контакты успешно добавлены");
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    @GetMapping("/{clientId}/contacts")
    public ResponseEntity<List<ContactDTO>> getContactsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getContactsByClientId(clientId));
    }

    @GetMapping("/{clientId}/contacts/{type}")
    public ResponseEntity<List<ContactDTO>> getContactsByType(@PathVariable Long clientId, @PathVariable ContactType type) {
        return ResponseEntity.ok(clientService.getContactsByType(clientId, type));
    }
}
