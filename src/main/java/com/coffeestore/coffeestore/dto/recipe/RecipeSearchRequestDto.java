package com.coffeestore.coffeestore.dto.recipe;

import lombok.Data;

@Data
public class RecipeSearchRequestDto {
    private String ingredientName;
    private String ingredientUnit;
}
