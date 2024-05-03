package com.example.testassignment.service;

import com.example.testassignment.datafilters.BirthdayRangeFiltering;
import com.example.testassignment.datafilters.PaginationParams;
import com.example.testassignment.datafilters.UserListFiltering;
import com.example.testassignment.entity.User;
import com.example.testassignment.helper.StringToDateConverter;
import com.example.testassignment.payload.UpdateUserPayload;
import com.example.testassignment.payload.UserPayload;
import com.example.testassignment.repositories.UserRepository;
import com.example.testassignment.servise.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldGetAllUsersWithNoFilters() {
        // Given
        var expectedList = List.of(new User(), new User());
        when(userRepository.getAll()).thenReturn(expectedList);

        // When
        var users = this.userService.getAllUsers(null);

        // Then
        Assertions.assertEquals(expectedList, users);
    }

    @Test
    public void shouldGetAllUsersWithEmptyFilters() {
        // Given
        var expectedList = List.of(new User(), new User());
        when(userRepository.getAll()).thenReturn(expectedList);

        // When
        var users = this.userService.getAllUsers(new UserListFiltering());

        // Then
        Assertions.assertEquals(expectedList, users);
    }

    @Test
    public void shouldGetAllUsersWithPagination() {
        var expectedList = generateUserList();
        when(userRepository.getAll()).thenReturn(expectedList);

        var users = this.userService.getAllUsers(new UserListFiltering(null, new PaginationParams(0, 0)));
        Assertions.assertEquals(expectedList, users);

        users = this.userService.getAllUsers(new UserListFiltering(null, new PaginationParams(0, 10)));
        Assertions.assertEquals(expectedList, users);

        users = this.userService.getAllUsers(new UserListFiltering(null, new PaginationParams(2, 2)));
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(expectedList.get(2), users.get(0));
        Assertions.assertEquals(expectedList.get(3), users.get(1));
    }

    @Test
    public void shouldGetAllUsersWithDateRange() {
        var expectedList = generateUserList();
        when(userRepository.getAll()).thenReturn(expectedList);

        var users = this.userService.getAllUsers(new UserListFiltering(new BirthdayRangeFiltering(StringToDateConverter.convert("1990-11-21"), null), null));
        Assertions.assertEquals(3, users.size());
        Assertions.assertEquals(expectedList.get(2), users.get(0));
        Assertions.assertEquals(expectedList.get(3), users.get(1));
        Assertions.assertEquals(expectedList.get(4), users.get(2));

        users = this.userService.getAllUsers(new UserListFiltering(new BirthdayRangeFiltering(null, StringToDateConverter.convert("1990-11-21")), null));
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(expectedList.get(0), users.get(0));
        Assertions.assertEquals(expectedList.get(1), users.get(1));


        users = this.userService.getAllUsers(new UserListFiltering(
                new BirthdayRangeFiltering(StringToDateConverter.convert("1990-01-21"),
                        StringToDateConverter.convert("1996-12-21")), null));
        Assertions.assertEquals(3, users.size());
        Assertions.assertEquals(expectedList.get(1), users.get(0));
        Assertions.assertEquals(expectedList.get(2), users.get(1));
        Assertions.assertEquals(expectedList.get(3), users.get(2));
    }


    @Test
    public void shouldGetUserById() {
        // Given
        var expectedUser = Optional.of(new User());
        when(userRepository.find(1)).thenReturn(expectedUser);

        // When
        var user = this.userService.getUser(1);

        // Then
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    public void shouldDeleteUserById() {
        // Given
        var deletingUser = new User();
        when(userRepository.find(1)).thenReturn(Optional.of(deletingUser));

        // When
        this.userService.deleteUser(1);

        // Then
        verify(userRepository).delete(deletingUser);
    }

    @Test
    public void shouldCreateUserById() {
        // When
        this.userService.createUser(new UserPayload());

        // Then
        verify(userRepository).create(any());
    }

    @Test
    public void shouldUpdateUser() {
        // Given
        var existUser = new User();
        when(userRepository.find(1)).thenReturn(Optional.of(existUser));

        // When
        this.userService.updateUser(1, new UpdateUserPayload());

        // Then
        verify(userRepository).update(existUser);
    }

    private List<User> generateUserList() {
        var user1 = new User(1, "", "", "");
        user1.setBirthday(StringToDateConverter.convert("1970-05-19"));
        var user2 = new User(2, "", "", "");
        user2.setBirthday(StringToDateConverter.convert("1990-10-21"));
        var user3 = new User(3, "", "", "");
        user3.setBirthday(StringToDateConverter.convert("1992-08-11"));
        var user4 = new User(4, "", "", "");
        user4.setBirthday(StringToDateConverter.convert("1996-03-15"));
        var user5 = new User(5, "", "", "");
        user5.setBirthday(StringToDateConverter.convert("2000-06-10"));

        return List.of(user1, user2, user3, user4, user5);
    }
}
