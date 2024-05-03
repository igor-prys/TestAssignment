package com.example.testassignment.validation;

import com.example.testassignment.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@Component
public class BirthdayValidator {
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";

    @Value("${user.age.min}")
    private Integer minUserAge;

    public void validate(String birthday) {
        validateFormat(birthday);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            var date = dateFormat.parse(birthday);

            if (date.after(new Date())) {
                throw new InvalidDataException("birthday should not be less than today");
            }

            if (!isAgeValid(date)) {
                throw new InvalidDataException("user should be older");
            }
        } catch (ParseException e) {
            throw new InvalidDataException("birthday format should be " + DATE_FORMAT);
        }
    }

    public void validateDateRanges(String birthdayFrom, String birthdayTo) {
        validateFormat(birthdayFrom);
        validateFormat(birthdayTo);
        if (birthdayFrom == null || birthdayTo == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            var date1 = dateFormat.parse(birthdayFrom);
            var date2 = dateFormat.parse(birthdayTo);

            if (date1.equals(date2) || date1.after(date2)) {
                throw new InvalidDataException("birthdayTo is less than birthdayFrom ");
            }
        } catch (ParseException e) {
            throw new InvalidDataException("birthday format should be " + DATE_FORMAT);
        }


    }

    private void validateFormat(String date) {
        if (date == null) {
            return;
        }
        var pattern = Pattern.compile(DATE_PATTERN);
        var matcher = pattern.matcher(date);

        if (!matcher.matches()) {
            throw new InvalidDataException("birthday format should be " + DATE_FORMAT);
        }
    }

    private boolean isAgeValid(Date date) {
        var birthday = Calendar.getInstance();
        birthday.setTime(date);
        var now = Calendar.getInstance();
        var age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        if (birthday.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
                (birthday.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                        birthday.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age >= minUserAge;
    }
}
