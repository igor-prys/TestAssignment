package com.example.testassignment.controllers;

import com.example.testassignment.datafilters.UserListFiltering;
import com.example.testassignment.entity.User;
import com.example.testassignment.exceptions.NoSuchUserException;
import com.example.testassignment.payload.UpdateUserPayload;
import com.example.testassignment.payload.UserPayload;
import com.example.testassignment.servise.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class TestAssignmentApplicationTests {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnEmptyListWhenNoDataExist() throws Exception {
        Mockito.when(userService.getAllUsers(any(UserListFiltering.class))).thenReturn(List.of());

        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(
                                """
                                        {"data":[]}"""
                        )));
    }

    @Test
    public void shouldGetListOfUsers() throws Exception {

        List<User> users = generateUserList();
        Mockito.when(userService.getAllUsers(any(UserListFiltering.class))).thenReturn(users);

        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].firstName", is("FIRST_NAME_1")))
                .andExpect(jsonPath("$.data[0].lastName", is("LAST_NAME_1")))
                .andExpect(jsonPath("$.data[1].firstName", is("FIRST_NAME_2")))
                .andExpect(jsonPath("$.data[1].lastName", is("LAST_NAME_2")));
    }

    @Test
    public void shouldFindUserById() throws Exception {
        var user = User.builder()
                .id(1)
                .firstName("FIRST_NAME_1")
                .lastName("LAST_NAME_1")
                .build();
        Mockito.when(userService.getUser(1)).thenReturn(Optional.of(user));
        this.mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.firstName", is("FIRST_NAME_1")))
                .andExpect(jsonPath("$.data.lastName", is("LAST_NAME_1")));
    }

    @Test
    public void shouldDeleteUserById() throws Exception {
        Mockito.doThrow(new NoSuchUserException()).when(userService).deleteUser(1);
        this.mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotDeleteUserIfItNotExist() throws Exception {
        var user = User.builder()
                .id(1)
                .firstName("FIRST_NAME_1")
                .lastName("LAST_NAME_1")
                .build();
        Mockito.when(userService.getUser(1)).thenReturn(Optional.of(user));
        this.mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddUser() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotAddUserIfFirstNameNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setFirstName(null);
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("firstName is required"));
    }

    @Test
    public void shouldNotAddUserIfLastNameNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setLastName(null);
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("lastName is required"));;
    }

    @Test
    public void shouldNotAddUserIfEmailNameNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setEmail(null);
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email is required"));;
    }

    @Test
    public void shouldNotAddUserIfEmailInvalid() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setEmail("invalidEmail");
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email is invalid"));;
    }

    @Test
    public void shouldNotAddUserIfBirthdayNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setBirthday(null);
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("birthday is required"));;
    }

    @Test
    public void shouldNotAddUserIfBirthdayInvalid() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setBirthday("invalidBirthday");
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("birthday format should be yyyy-MM-dd"));;
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotUpdateUserIfUserNotExist() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        Mockito.doThrow(new NoSuchUserException()).when(userService).updateUser(1, userPayload);
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotUpdateUserIfUsernameNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setFirstName(null);
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("firstName is required"));;
    }

    @Test
    public void shouldNotUpdateUserIfLastNameNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setLastName(null);
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("lastName is required"));;
    }

    @Test
    public void shouldNotUpdateUserIfEmailNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setEmail(null);
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email is required"));;
    }

    @Test
    public void shouldNotUpdateUserIfEmailIsInvalid() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setEmail("invalidEmail");
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email is invalid"));;
    }

    @Test
    public void shouldNotUpdateUserIfBirthdayNotDefined() throws Exception {
        UserPayload userPayload = generateValidUserPayload();
        userPayload.setBirthday(null);
        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("birthday is required"));;
    }

    @Test
    public void shouldUpdateUserPartially() throws Exception {
        var userPayload = generateUserPayloadWithRequiredFields();
        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatePartiallyWithoutRequiredFields() throws Exception {
        var userPayload = new UpdateUserPayload();
        userPayload.setPhoneNumber("+1234567890");
        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotUpdatePartiallyWithBlankFirstName() throws Exception {
        var userPayload = generateUserPayloadWithRequiredFields();
        userPayload.setFirstName("");

        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("firstName can't be blank"));
    }

    @Test
    public void shouldNotUpdatePartiallyWithBlankLastName() throws Exception {
        var userPayload = generateUserPayloadWithRequiredFields();
        userPayload.setLastName("");

        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("lastName can't be blank"));
    }

    @Test
    public void shouldNotUpdatePartiallyWithInvalidEmail() throws Exception {
        var userPayload = generateUserPayloadWithRequiredFields();
        userPayload.setEmail("invalid");

        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email is invalid"));
    }

    @Test
    public void shouldNotUpdatePartiallyWithInvalidBirthday() throws Exception {
        var userPayload = generateUserPayloadWithRequiredFields();
        userPayload.setBirthday("invalidBirthday");

        this.mockMvc.perform(patch("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("birthday format should be yyyy-MM-dd"));
    }

    private List<User> generateUserList() {
        var user1 = User.builder()
                .id(1)
                .firstName("FIRST_NAME_1")
                .lastName("LAST_NAME_1")
                .build();
        var user2 = User.builder()
                .id(1)
                .firstName("FIRST_NAME_2")
                .lastName("LAST_NAME_2")
                .build();
        return List.of(user1, user2);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UpdateUserPayload generateUserPayloadWithRequiredFields() {
        var user = new UpdateUserPayload();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.email");
        return user;
    }

    private UserPayload generateValidUserPayload() {
        return new UserPayload("firstName", "lastName",
                "email@email.email", "1970-03-25");
    }
}
