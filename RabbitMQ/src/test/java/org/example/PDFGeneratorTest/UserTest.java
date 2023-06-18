package org.example.PDFGeneratorTest;

import org.example.PDFGenerator.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testGetId() {

        int expectedId = 1;
        User user = new User(expectedId, "John", "Doe");

        int actualId = user.getId();

        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void testFnLn() {
        String firstName = "John";
        String lastName = "Doe";
        User user = new User(1, firstName, lastName);

        String fullName = user.fnLn();

        String expectedFullName = firstName + " " + lastName;
        Assertions.assertEquals(expectedFullName, fullName);
    }

    @Test
    public void testToString() {
        int id = 1;
        String firstName = "John";
        String lastName = "Doe";
        User user = new User(id, firstName, lastName);



        String userString = user.toString();

        String expectedString = "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
        Assertions.assertEquals(expectedString, userString);
    }
}

