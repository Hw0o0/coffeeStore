package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.ingredient.IngredientRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.ingredient.IngredientUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
}
