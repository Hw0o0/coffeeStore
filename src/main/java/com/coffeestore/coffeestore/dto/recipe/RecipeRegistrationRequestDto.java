package com.coffeestore.coffeestore.dto.recipe;

import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class RecipeRegistrationRequestDto {

    private int amount;

    public Recipe toEntity(Optional<Menu> menu, Optional<Ingredient> ingredient){
        return Recipe.builder()
                .menu(menu.get())
                .ingredient(ingredient.get())
                .amount(amount)
                .build();
    }
}
