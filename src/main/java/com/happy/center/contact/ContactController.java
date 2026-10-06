package com.happy.center.contact;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Public endpoint used by the website's contact form.
     * There is deliberately no public GET: leads contain personal data.
     * Add an authenticated admin endpoint (Spring Security) before exposing them.
     */
    @PostMapping
    public ResponseEntity<ContactCreatedResponse> create(@Valid @RequestBody CreateContactRequest request) {
        ContactLead lead = contactService.create(request);
        return ResponseEntity
                .created(URI.create("/api/contacts/" + lead.getId()))
                .body(ContactCreatedResponse.from(lead));
    }
}
