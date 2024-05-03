package com.example.testassignment.datafilters;

import com.example.testassignment.helper.StringToDateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirthdayRangeFiltering {
    private Date from;
    private Date to;

    public BirthdayRangeFiltering(String from, String to) {
        this.from = Optional.ofNullable(from)
                .map(StringToDateConverter::convert)
                .orElse(null);
        this.to = Optional.ofNullable(to)
                .map(StringToDateConverter::convert)
                .orElse(null);
    }
}
