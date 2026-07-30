package com.preparegh200;

import com.preparegh200.model.User;
import com.preparegh200.repository.UserRepository;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserCrudIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/users";
    }

    private User createUser(String firstName, String lastName, String email) {
        User request = new User(null, firstName, lastName, email);
        ResponseEntity<User> createResponse = restTemplate.postForEntity(URI.create(getBaseUrl()), request, User.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();
        return createResponse.getBody();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        printDatabaseDetails();
        printUserTable("BEFORE");
    }

    @AfterEach
    void afterEach() throws SQLException {
        printUserTable("AFTER");
        userRepository.deleteAll();
    }

    private void printUserTable(String phase) throws SQLException {
        System.out.println("=== USER TABLE " + phase + " TEST ===");
        try (Connection connection = dataSource.getConnection();
             java.sql.Statement statement = connection.createStatement();
             java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean empty = true;

            while (resultSet.next()) {
                empty = false;
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        row.append(" | ");
                    }
                    row.append(metaData.getColumnName(i)).append("=").append(resultSet.getString(i));
                }
                System.out.println(row);
            }

            if (empty) {
                System.out.println("(empty)");
            }
        }
        System.out.println("===============================");
    }

    private void printDatabaseDetails() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("=== DATABASE DETAILS ===");
            System.out.println("URL: " + metaData.getURL());
            System.out.println("User: " + metaData.getUserName());
            System.out.println("DatabaseProductName: " + metaData.getDatabaseProductName());
            System.out.println("DatabaseProductVersion: " + metaData.getDatabaseProductVersion());
            System.out.println("DriverName: " + metaData.getDriverName());
            System.out.println("DriverVersion: " + metaData.getDriverVersion());
            System.out.println("========================");
        }
    }

    @Test
    void createReadUpdateDeleteUserLifecycle() {
        User createdUser = createUser("Alice", "Smith", "alice@example.com");
        System.out.println("Created user: " + createdUser.getId() + " - " + createdUser.getFirstName() + " " + createdUser.getLastName() + " <" + createdUser.getEmail() + ">");

        ResponseEntity<User> readResponse = restTemplate.getForEntity(URI.create(getBaseUrl() + "/" + createdUser.getId()), User.class);
        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        User readUser = readResponse.getBody();
        assertThat(readUser).isNotNull();
        assertThat(readUser.getEmail()).isEqualTo("alice@example.com");
        System.out.println("Retrieved user: " + readUser.getId() + " - " + readUser.getFirstName() + " " + readUser.getLastName() + " <" + readUser.getEmail() + ">");

        User updateRequest = new User(null, "Alice", "Walker", "alice.walker@example.com");
        ResponseEntity<User> updateResponse = restTemplate.exchange(
                URI.create(getBaseUrl() + "/" + createdUser.getId()),
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updateRequest),
                User.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        User updatedUser = updateResponse.getBody();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getLastName()).isEqualTo("Walker");
        assertThat(updatedUser.getEmail()).isEqualTo("alice.walker@example.com");
        System.out.println("Updated user: " + updatedUser.getId() + " - " + updatedUser.getFirstName() + " " + updatedUser.getLastName() + " <" + updatedUser.getEmail() + ">");

        restTemplate.delete(URI.create(getBaseUrl() + "/" + createdUser.getId()));
        System.out.println("Deleted user: " + createdUser.getId());

        ResponseEntity<String> deleteCheck = restTemplate.getForEntity(URI.create(getBaseUrl() + "/" + createdUser.getId()), String.class);
        assertThat(deleteCheck.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
