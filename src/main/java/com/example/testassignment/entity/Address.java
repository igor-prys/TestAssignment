package com.example.testassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {

    private String country;
    private String city;
    private String street;
    private int houseNumber;
    private int zipCode;

}
