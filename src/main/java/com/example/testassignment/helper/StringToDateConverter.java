package com.example.testassignment.helper;

import com.example.testassignment.exceptions.InvalidDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter {
    public static Date convert(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new InvalidDataException("Invalid date " + date);
        }
    }
}
