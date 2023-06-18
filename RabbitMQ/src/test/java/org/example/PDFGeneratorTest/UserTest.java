package org.example.PDFGeneratorTest;

import org.example.PDFGenerator.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testGetId() {
        // Arrange
        int expectedId = 1;
        User user = new User(expectedId, "John", "Doe");

        // Act
        int actualId = user.getId();

        // Assert
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void testFnLn() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        User user = new User(1, firstName, lastName);

        // Act
        String fullName = user.fnLn();

        // Assert
        String expectedFullName = firstName + " " + lastName;
        Assertions.assertEquals(expectedFullName, fullName);
    }

    @Test
    public void testToString() {
        // Arrange
        int id = 1;
        String firstName = "John";
        String lastName = "Doe";
        User user = new User(id, firstName, lastName);

        // Act
        String userString = user.toString();

        // Assert
        String expectedString = "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
        Assertions.assertEquals(expectedString, userString);
    }
}

