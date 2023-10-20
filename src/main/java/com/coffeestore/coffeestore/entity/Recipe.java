package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.recipe.RecipeUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Ingredient ingredient;

    @Column(nullable = false)   //사용량
    private int amount;


    public void update(RecipeUpdateRequestDto recipeUpdateRequestDto) {
        this.amount = recipeUpdateRequestDto.getUpdateAmount();
    }
}
