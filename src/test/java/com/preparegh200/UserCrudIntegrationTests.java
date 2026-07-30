package com.preparegh200;

import com.preparegh200.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserCrudIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Test
    void createReadDeleteUserCrudOperations() {
        User request = new User(null, "Alice", "Smith", "alice@example.com");

        ResponseEntity<User> createResponse = restTemplate.postForEntity(URI.create(getBaseUrl()), request, User.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();

        Long userId = createResponse.getBody().getId();

        ResponseEntity<User> readResponse = restTemplate.getForEntity(URI.create(getBaseUrl() + "/" + userId), User.class);
        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResponse.getBody()).isNotNull();
        assertThat(readResponse.getBody().getEmail()).isEqualTo("alice@example.com");

        restTemplate.delete(URI.create(getBaseUrl() + "/" + userId));

        ResponseEntity<String> deleteCheck = restTemplate.getForEntity(URI.create(getBaseUrl() + "/" + userId), String.class);
        assertThat(deleteCheck.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void listUsersReturnsSavedUser() {
        User request = new User(null, "Bob", "Johnson", "bob@example.com");

        ResponseEntity<User> createResponse = restTemplate.postForEntity(URI.create(getBaseUrl()), request, User.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();

        ResponseEntity<User[]> listResponse = restTemplate.getForEntity(URI.create(getBaseUrl()), User[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).isNotNull();
        assertThat(listResponse.getBody()).extracting(User::getEmail).contains("bob@example.com");
    }
}
