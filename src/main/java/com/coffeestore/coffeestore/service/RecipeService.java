package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.recipe.RecipeRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.recipe.RecipeUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.MenuRepository;
import com.coffeestore.coffeestore.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final MenuRepository menuRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public List<Recipe> findByMenuUseAll(Long menuId){    //요리에 사용되는 재료들
        List<Recipe> recipeList = recipeRepository.findAll()
                .stream()
                .filter(Recipe -> Recipe.getMenu().getId().equals(menuId))
                .collect(Collectors.toList());
        if(recipeList.isEmpty()){
            return recipeList;
        }else {
            return null;
        }
    }
    public Recipe findByRecipe(Long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.orElse(null);
    }
    public void createByRecipe(Long MenuId, Long ingredientId, RecipeRegistrationRequestDto recipeRegistrationRequestDto){
        Optional<Menu> menu = menuRepository.findById(MenuId);
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        Recipe recipe = recipeRegistrationRequestDto.toEntity(menu,ingredient);
        recipeRepository.save(recipe);
    }
    public void updateByRecipe(Long id, RecipeUpdateRequestDto recipeUpdateRequestDto){
       Recipe recipe = findByRecipe(id);
        recipe.update(recipeUpdateRequestDto);
        recipeRepository.save(recipe);
    }

    public void deleteByRecipe(Long id){
        Recipe recipe = findByRecipe(id);
        recipeRepository.delete(recipe);
    }
}
