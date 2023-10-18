package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

}
