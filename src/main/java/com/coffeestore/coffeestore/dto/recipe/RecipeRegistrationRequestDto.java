package com.coffeestore.coffeestore.dto.recipe;

import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RecipeRegistrationRequestDto {

    private Integer amount;

    public static Recipe toEntity(Menu menu, Ingredient ingredient,Integer amount){
        return Recipe.builder()
                .menu(menu)
                .ingredient(ingredient)
                .amount(amount)
                .build();
    }
}
