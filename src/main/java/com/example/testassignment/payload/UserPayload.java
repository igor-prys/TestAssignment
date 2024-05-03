package com.example.testassignment.payload;

import com.example.testassignment.entity.Address;
import com.example.testassignment.validation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayload {
    @NotNull(message = "email is required")
    @Email(message = "email is invalid")
    private String email;
    @NotNull(message = "firstName is required")
    private String firstName;
    @NotNull(message = "lastName is required")
    private String lastName;
    @NotNull(message = "birthday is required")
    private String birthday;
    private Address address;
    @Phone
    private String phoneNumber;

    public UserPayload(String firstName, String lastName, String email, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }
}
