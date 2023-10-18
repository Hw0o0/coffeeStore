package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.ingredient.IngredientRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.ingredient.IngredientUpdateRequestDto;
import com.coffeestore.coffeestore.dto.recipe.RecipeSearchRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> findByAll(){
        return ingredientRepository.findAll();
    }

    public Ingredient findByIngredient(Long id){
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        return ingredient.orElse(null);
    }

    public void createByIngredient(IngredientRegistrationRequestDto ingredientRegistrationRequestDto){
        Ingredient ingredient = ingredientRegistrationRequestDto.toEntity();
        ingredientRepository.save(ingredient);
    }
    public void updateByIngredient(Long id,IngredientUpdateRequestDto ingredientUpdateRequestDto){
        Ingredient ingredient = findByIngredient(id);
        ingredient.update(ingredientUpdateRequestDto);
        ingredientRepository.save(ingredient);
    }
    public void deleteByIngredient(Long id){
        Ingredient ingredient = findByIngredient(id);
        ingredient.setState(0);
        ingredientRepository.save(ingredient);
    }

    public void stateChangeByIngredient(Long ingredientId) {
        Ingredient ingredient = findByIngredient(ingredientId);
        if(ingredient.getState() == 0){
            ingredient.setState(1);
        }else {
            ingredient.setState(0);
        }
        ingredientRepository.save(ingredient);
    }

    public void ingredientSearch(Model model, RecipeSearchRequestDto recipeSearchRequestDto){
        List<Ingredient> ingredientList = new ArrayList<>();
        //패턴을 컴파일한다.
        Pattern name = Pattern.compile(recipeSearchRequestDto.getName());
        Pattern unit = Pattern.compile(recipeSearchRequestDto.getUnit());
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (Ingredient ingredient : findByAll()) {
            Matcher nameMatcher = name.matcher(recipeSearchRequestDto.getName());
            Matcher unitMatcher = name.matcher(recipeSearchRequestDto.getName());
            if (nameMatcher.find()&&unitMatcher.find()) {
                ingredientList.add(ingredient);
            }
        }
        model.addAttribute("ingredientSearch",ingredientList);
    }
}
