package com.coffeestore.coffeestore.dto.ingredient;

import lombok.Data;

@Data
public class IngredientUpdateRequestDto {
    private String updateName;
    private Integer updateAmount;
    private String updateUnit;
}
