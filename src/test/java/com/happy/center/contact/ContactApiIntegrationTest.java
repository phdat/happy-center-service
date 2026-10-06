package com.happy.center.contact;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/** Starts the real app on a random port and calls it over HTTP, like the Angular app does. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactApiIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ContactLeadRepository repository;

    private final HttpClient http = HttpClient.newHttpClient();

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private HttpResponse<String> post(String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/contacts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void createsLeadAndNormalizesPhone() throws Exception {
        HttpResponse<String> res = post("""
                {"audience":"CHILD","fullName":"  Nguyễn Văn An ","phone":"+84 912.345.678",
                 "email":"","courses":["KIDS","TOEIC"],"note":"Bé 8 tuổi"}
                """);

        assertThat(res.statusCode()).isEqualTo(201);
        assertThat(res.headers().firstValue("Location")).hasValueSatisfying(l -> assertThat(l).startsWith("/api/contacts/"));
        assertThat(res.body()).contains("\"id\"").contains("\"createdAt\"");

        assertThat(repository.findAll()).singleElement().satisfies(lead -> {
            assertThat(lead.getFullName()).isEqualTo("Nguyễn Văn An");
            assertThat(lead.getPhone()).isEqualTo("0912345678");
            assertThat(lead.getEmail()).isNull();
            assertThat(lead.getCourses()).containsExactlyInAnyOrder(Course.KIDS, Course.TOEIC);
            assertThat(lead.getStatus()).isEqualTo(ContactStatus.NEW);
            assertThat(lead.getCreatedAt()).isNotNull();
        });
    }

    @Test
    void rejectsInvalidInputWithFieldErrors() throws Exception {
        HttpResponse<String> res = post("""
                {"audience":"SELF","fullName":"","phone":"12345","email":"not-an-email","courses":[]}
                """);

        assertThat(res.statusCode()).isEqualTo(400);
        assertThat(res.body()).contains("\"errors\"").contains("\"fullName\"").contains("\"phone\"").contains("\"email\"");
        assertThat(repository.count()).isZero();
    }

    @Test
    void rejectsUnknownCourse() throws Exception {
        HttpResponse<String> res = post("""
                {"audience":"SELF","fullName":"Lan","phone":"0912345678","courses":["PIANO"]}
                """);

        assertThat(res.statusCode()).isEqualTo(400);
        assertThat(repository.count()).isZero();
    }
}
