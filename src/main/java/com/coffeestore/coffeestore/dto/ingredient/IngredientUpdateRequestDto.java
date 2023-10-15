package com.coffeestore.coffeestore.dto.ingredient;

import lombok.Data;

@Data
public class IngredientUpdateRequestDto {
    private String name;
    private int amount;
    private String unit;
}
