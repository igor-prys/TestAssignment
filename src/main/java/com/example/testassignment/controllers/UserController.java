package com.example.testassignment.controllers;

import com.example.testassignment.datafilters.BirthdayRangeFiltering;
import com.example.testassignment.datafilters.PaginationParams;
import com.example.testassignment.datafilters.UserListFiltering;
import com.example.testassignment.dto.UserDto;
import com.example.testassignment.dto.UsersListDto;
import com.example.testassignment.exceptions.InvalidDataException;
import com.example.testassignment.exceptions.NoSuchUserException;
import com.example.testassignment.payload.UpdateUserPayload;
import com.example.testassignment.payload.UserPayload;
import com.example.testassignment.servise.UserService;
import com.example.testassignment.validation.BirthdayValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final BirthdayValidator birthdayValidator;

    @GetMapping
    public UsersListDto getAllUsers(@RequestParam(required = false) String from,
                                    @RequestParam(required = false) String to,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "0") int limit) {
        birthdayValidator.validateDateRanges(from, to);
        var rangeFiltering = new BirthdayRangeFiltering(from, to);
        var pagination = new PaginationParams(offset, limit);
        var filtering = new UserListFiltering(rangeFiltering, pagination);
        return new UsersListDto(userService.getAllUsers(filtering));
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") int id) {
        UserDto userDto = new UserDto(userService.getUser(id)
                .orElseThrow(() -> new NoSuchUserException()));
        return userDto;
    }

    @PostMapping
    public void addUser(@Valid @RequestBody UserPayload userPayload,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errorMessage = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .findAny()
                    .orElse("Invalid data");
            throw new InvalidDataException(errorMessage);
        }
        birthdayValidator.validate(userPayload.getBirthday());
        userService.createUser(userPayload);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable("userId") int id,
                           @Valid @RequestBody UserPayload userPayload,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errorMessage = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .findAny()
                    .orElse("Invalid data");
            throw new InvalidDataException(errorMessage);
        }
        birthdayValidator.validate(userPayload.getBirthday());
        userService.updateUser(id, userPayload);
    }


    @PatchMapping("/{userId}")
    public void updatePartiallyUser(@PathVariable("userId") int userId,
                                    @Valid @RequestBody UpdateUserPayload userPayload,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            var errorMessage = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .findAny()
                    .orElse("Invalid data");
            throw new InvalidDataException(errorMessage);
        }
        if (userPayload.getBirthday() != null) {
            birthdayValidator.validate(userPayload.getBirthday());
        }
        userService.updateUser(userId, userPayload);
    }
}
