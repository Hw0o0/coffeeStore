package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.recipe.RecipeRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.recipe.RecipeUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import com.coffeestore.coffeestore.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public List<Recipe> findByMenuUseAll(Long menuId) {    //요리에 사용되는 재료들
        return recipeRepository.findRecipeByMenu_Id(menuId);
    }

    public Recipe findByRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    public void createByRecipe(Menu menu, Ingredient ingredient, Integer amount) {
        Recipe recipe = RecipeRegistrationRequestDto.toEntity(menu, ingredient, amount);
        recipeRepository.save(recipe);
    }

    public void updateByRecipe(Long recipeId, RecipeUpdateRequestDto recipeUpdateRequestDto) {
        Recipe recipe = findByRecipe(recipeId);
        recipe.update(recipeUpdateRequestDto);
        recipeRepository.save(recipe);
    }

    public void deleteByRecipe(Long id) {
        Recipe recipe = findByRecipe(id);
        recipeRepository.delete(recipe);
    }
}
