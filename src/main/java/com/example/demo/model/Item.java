package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Item {

    /*public Item(ItemPk itemPk, String name, String snakeCase, String camelCase) {
        this.itemPk = itemPk;
        if(itemPk != null) {
            this.itemNo = itemPk.getItemNo();
            this.effectiveStartDate = itemPk.getEffectiveStartDate();
        }
        this.name = name;
        this.snakeCase = snakeCase;
        this.camelCase = camelCase;
    }*/

    public Item(Integer itemNo, String effectiveStartDate, String name, String snakeCase, String camelCase) {
        //this.itemPk = new ItemPk(itemNo, effectiveStartDate);
        this.itemNo = itemNo;
        this.effectiveStartDate = effectiveStartDate;
        this.name = name;
        this.snakeCase = snakeCase;
        this.camelCase = camelCase;
        this.itemRegisterStatus = new ItemRegisterStatus();
    }

    public Item(Integer itemNo, String effectiveStartDate, String name, String snakeCase, String camelCase, Integer noDup, String nameDup) {
        this.itemNo = itemNo;
        this.effectiveStartDate = effectiveStartDate;
        this.name = name;
        this.snakeCase = snakeCase;
        this.camelCase = camelCase;
        this.itemRegisterStatus = new ItemRegisterStatus(noDup, nameDup);
    }

    //private ItemPk itemPk;
    @Id
    private Integer itemNo;
    private String effectiveStartDate;
    private String name;
    private String snakeCase;
    private String camelCase;

    @Transient
    private ItemRegisterStatus itemRegisterStatus;
}
