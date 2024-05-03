package com.example.testassignment.dto;

import com.example.testassignment.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersListDto {
    private List<User> data;
}
