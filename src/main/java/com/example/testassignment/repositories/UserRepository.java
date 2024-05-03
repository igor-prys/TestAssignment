package com.example.testassignment.repositories;

import com.example.testassignment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> find(int id);

    void create(User user);

    void delete(User user);

    void update(User user);
}
