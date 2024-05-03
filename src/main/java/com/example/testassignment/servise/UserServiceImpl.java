package com.example.testassignment.servise;

import com.example.testassignment.datafilters.UserListFiltering;
import com.example.testassignment.entity.User;
import com.example.testassignment.exceptions.NoSuchUserException;
import com.example.testassignment.helper.UserPatcher;
import com.example.testassignment.payload.UpdateUserPayload;
import com.example.testassignment.payload.UserPayload;
import com.example.testassignment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<User> getAllUsers(UserListFiltering filter) {
        var users = userRepository.getAll();
        if (filter != null) {
            var filteredUsers = Optional.ofNullable(filter.getRange())
                    .map((r) -> users.stream()
                            .filter(u -> r.getFrom() == null || u.getBirthday().after(r.getFrom()))
                            .filter(u -> r.getTo() == null || u.getBirthday().before(r.getTo()))
                            .toList())
                    .orElse(users);
            if (filter.getPagination() != null) {
                var from = filter.getPagination().getOffset();
                if (from >= filteredUsers.size()) {
                    return List.of();
                }
                var limit = filter.getPagination().getLimit();
                return filteredUsers.stream()
                        .skip(from)
                        .limit(limit == 0 ? filteredUsers.size() : limit)
                        .toList();
            }
            return filteredUsers;
        }
        return users;
    }

    @Override
    public void createUser(UserPayload userPayload) {
        User user = modelMapper.map(userPayload, User.class);
        userRepository.create(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.find(id)
                .ifPresentOrElse(u -> userRepository.delete(u), NoSuchUserException::new);
    }

    @Override
    public Optional<User> getUser(int id) {
        return userRepository.find(id);
    }

    @Override
    public void updateUser(int id, UserPayload userPayload) {
        Optional<User> existUser = getUser(id);
        if (existUser.isEmpty()) {
            throw new NoSuchUserException("User doesn't exist");
        }
        User user = modelMapper.map(userPayload, User.class);
        user.setId(existUser.get().getId());
        userRepository.update(user);
    }

    @Override
    public void updateUser(int id, UpdateUserPayload userUpdates) {
        User foundUser = getUser(id).orElseThrow(NoSuchUserException::new);
        UserPatcher.apply(foundUser, userUpdates);
        userRepository.update(foundUser);
    }
}
