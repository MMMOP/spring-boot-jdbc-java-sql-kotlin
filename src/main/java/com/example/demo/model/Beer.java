package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class Beer {
    private String name;
    private Integer no;
    private String snakeCase;
    private String camelCase;
}
