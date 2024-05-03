package com.example.testassignment.repositories;

import com.example.testassignment.entity.User;
import com.example.testassignment.exceptions.NoSuchUserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;
    private User user;

    @BeforeEach
    public void before() {
        this.repository = new InMemoryUserRepository();
        user = new User(1, "FIRST_NAME 1", "LAST_NAME 1", "EMAIL 1");

    }

    @Test
    public void shouldFindUserById() {
        // Given
        repository.create(user);

        //When
        var foundUser = this.repository.find(1);

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        var user2 = new User(1, "FIRST_NAME 2", "LAST_NAME 2", "EMAIL 2");
        repository.create(user);
        repository.create(user2);

        //When
        this.repository.delete(user);

        //Then
        var foundUser = this.repository.find(1);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void addUserTest() {
        // When
        this.repository.create(user);

        // Then
        var foundUser = this.repository.find(1);
        assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user, foundUser.get());
    }

    @Test
    public void getAllUsersTest() {
        // Given
        var user1 = new User(1, "FIRST_NAME 1", "LAST_NAME 1", "EMAIL 1");
        var user2 = new User(2, "FIRST_NAME 2", "LAST_NAME 2", "EMAIL 2");
        repository.create(user1);
        repository.create(user2);

        // When
        var result = repository.getAll();

        // Then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void shouldReplace() {
        repository.create(user);
        var newUser = new User(1, "FIRST_NAME 2", "LAST_NAME 2", "EMAIL 2");

        repository.update(newUser);

        Assertions.assertEquals("FIRST_NAME 2", repository.find(1).get().getFirstName());
    }

    @Test
    public void shouldNotReplaceNotExistUser() {
        repository.create(user);
        var newUser = new User(12, "FIRST_NAME 2", "LAST_NAME 2", "EMAIL 2");

        NoSuchUserException thrown = assertThrows(
                NoSuchUserException.class,
                () -> repository.update(newUser),
                "Expected replaceUser() to trow exception for not exist user"
        );

        assertTrue(thrown.getMessage().contains("User is not found"));
    }
}