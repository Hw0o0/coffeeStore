package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
}
