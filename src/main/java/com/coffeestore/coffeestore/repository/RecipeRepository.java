package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    List<Recipe> findRecipeByMenu_Id(Long menuId);
}
