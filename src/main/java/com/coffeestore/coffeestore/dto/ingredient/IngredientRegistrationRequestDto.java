package com.coffeestore.coffeestore.dto.ingredient;

import com.coffeestore.coffeestore.entity.Ingredient;
import lombok.Data;

import java.util.Date;

@Data
public class IngredientRegistrationRequestDto {
        private String name;
        private Integer amount;
        private String unit;

    public Ingredient toEntity() {
        return Ingredient.builder()
                .name(name)
                .amount(amount)
                .unit(unit)
                .state(1)
                .createdDate(new Date())
                .build();
    }
}
