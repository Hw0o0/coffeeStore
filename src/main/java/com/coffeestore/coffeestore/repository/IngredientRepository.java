package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

    Optional<Ingredient> findByName(String menuName);

    Optional<Ingredient> findByNameAndUnit(String ingredientName, String ingredientUnit);
}
