package com.happy.center.contact;

import java.util.EnumSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactLeadRepository repository;

    public ContactService(ContactLeadRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContactLead create(CreateContactRequest request) {
        Set<Course> courses = request.courses() == null || request.courses().isEmpty()
                ? EnumSet.noneOf(Course.class)
                : EnumSet.copyOf(request.courses());

        ContactLead lead = new ContactLead(
                request.audience(),
                request.fullName().trim(),
                normalizePhone(request.phone()),
                blankToNull(request.email()),
                courses,
                blankToNull(request.note()));

        ContactLead saved = repository.save(lead);
        // Don't log personal data (name/phone) — id is enough to look it up.
        log.info("New contact lead #{} for courses {}", saved.getId(), saved.getCourses());
        return saved;
    }

    /** "+84 912.345.678" → "0912345678". */
    static String normalizePhone(String phone) {
        String digits = phone.replaceAll("[\\s.-]", "");
        return digits.startsWith("+84") ? "0" + digits.substring(3) : digits;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
