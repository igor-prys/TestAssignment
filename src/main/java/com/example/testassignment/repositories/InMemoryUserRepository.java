package com.example.testassignment.repositories;

import com.example.testassignment.entity.User;
import com.example.testassignment.exceptions.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> userList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public List<User> getAll() {
        return Collections.unmodifiableList(userList);
    }

    @Override
    public Optional<User> find(int id) {
        return userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() == user.getId()) {
                userList.set(i, user);
                return;
            }
        }
        throw new NoSuchUserException("User is not found");
    }

    @Override
    public void create(User user) {
        user.setId(userList.stream()
                .max(Comparator.comparingInt(User::getId))
                .map(User::getId)
                .orElse(0) + 1);
        userList.add(user);
    }

    @Override
    public void delete(User user) {
        userList.remove(user);
    }
}
