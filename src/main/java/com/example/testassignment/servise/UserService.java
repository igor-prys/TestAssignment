package com.example.testassignment.servise;

import com.example.testassignment.datafilters.UserListFiltering;
import com.example.testassignment.entity.User;
import com.example.testassignment.payload.UpdateUserPayload;
import com.example.testassignment.payload.UserPayload;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers(UserListFiltering filter);

    Optional<User> getUser(int id);

    void createUser(UserPayload userPayload);

    void deleteUser(int id);

    void updateUser(int id, UpdateUserPayload userPayload);

    void updateUser(int id, UserPayload userPayload);
}
