package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.ingredient.IngredientUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    public void update(IngredientUpdateRequestDto ingredientUpdateRequestDto){
        this.name = ingredientUpdateRequestDto.getName();
        this.amount = ingredientUpdateRequestDto.getAmount();
        this.unit = ingredientUpdateRequestDto.getUnit();
        this.modificationDate = new Date();
    }
}
