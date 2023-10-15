package com.coffeestore.coffeestore.dto.menu;

import lombok.Data;

@Data
public class MenuUpdateRequestDto {
    private String name;

    private int price;

    private String categorize;
}
