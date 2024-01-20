package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Item {

    public Item(Integer no, String name, String snakeCase, String camelCase, Integer noDup, String nameDup) {
        this.no = no;
        this.name = name;
        this.snakeCase = snakeCase;
        this.camelCase = camelCase;
        this.itemRegisterStatus = new ItemRegisterStatus(noDup, nameDup);
    }

    @Id
    private Integer no;
    private String name;
    private String snakeCase;
    private String camelCase;

    @Transient
    private ItemRegisterStatus itemRegisterStatus;
}
