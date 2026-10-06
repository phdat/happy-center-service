package com.happy.center.contact;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

/** A consultation request left on the website. */
@Entity
@Table(name = "contact_lead")
public class ContactLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ContactAudience audience;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 150)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_lead_course", joinColumns = @JoinColumn(name = "lead_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "course", nullable = false, length = 20)
    private Set<Course> courses = EnumSet.noneOf(Course.class);

    @Column(length = 1000)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContactStatus status = ContactStatus.NEW;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ContactLead() {
        // for JPA
    }

    public ContactLead(ContactAudience audience, String fullName, String phone, String email,
                       Set<Course> courses, String note) {
        this.audience = audience;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.courses = courses.isEmpty() ? EnumSet.noneOf(Course.class) : EnumSet.copyOf(courses);
        this.note = note;
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() { return id; }
    public ContactAudience getAudience() { return audience; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Set<Course> getCourses() { return courses; }
    public String getNote() { return note; }
    public ContactStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void setStatus(ContactStatus status) { this.status = status; }
}
