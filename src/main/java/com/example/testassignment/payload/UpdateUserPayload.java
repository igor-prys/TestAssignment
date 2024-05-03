package com.example.testassignment.payload;

import com.example.testassignment.entity.Address;
import com.example.testassignment.validation.NotEmptyOrNull;
import com.example.testassignment.validation.Phone;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPayload {
    @Email(message = "email is invalid")
    private String email;
    @NotEmptyOrNull(message = "firstName can't be blank")
    private String firstName;
    @NotEmptyOrNull(message = "lastName can't be blank")
    private String lastName;
    @NotEmptyOrNull(message = "birthday can't be blank")
    private String birthday;
    private Address address;
    @Phone
    private String phoneNumber;
}
