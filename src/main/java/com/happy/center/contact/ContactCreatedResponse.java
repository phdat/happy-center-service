package com.happy.center.contact;

import java.time.Instant;

public record ContactCreatedResponse(Long id, Instant createdAt) {

    static ContactCreatedResponse from(ContactLead lead) {
        return new ContactCreatedResponse(lead.getId(), lead.getCreatedAt());
    }
}
