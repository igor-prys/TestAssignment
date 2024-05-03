package com.example.testassignment.datafilters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationParams {
    private int offset;
    private int limit;
}
