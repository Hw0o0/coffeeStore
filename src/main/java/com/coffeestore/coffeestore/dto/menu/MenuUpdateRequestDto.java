package com.coffeestore.coffeestore.dto.menu;

import lombok.Data;

@Data
public class MenuUpdateRequestDto {
    private String updateName;

    private Integer updatePrice;

    private String updateCategorize;
}
