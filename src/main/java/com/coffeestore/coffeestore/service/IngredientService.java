package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.ingredient.IngredientRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.ingredient.IngredientUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> findByAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient findByIngredient(Long id) {
        return ingredientRepository.findById(id).orElseThrow();
    }

    public void createByIngredient(IngredientRegistrationRequestDto ingredientRegistrationRequestDto) {
        Ingredient ingredient = ingredientRegistrationRequestDto.toEntity();
        ingredientRepository.save(ingredient);
    }

    public void updateByIngredient(Long id, IngredientUpdateRequestDto ingredientUpdateRequestDto) {
        Ingredient ingredient = findByIngredient(id);
        ingredient.update(ingredientUpdateRequestDto);
        ingredientRepository.save(ingredient);
    }

    public void deleteByIngredient(Long id) {
        Ingredient ingredient = findByIngredient(id);
        ingredient.setState(0);
        ingredientRepository.save(ingredient);
    }

    public void stateChangeByIngredient(Long ingredientId) {
        Ingredient ingredient = findByIngredient(ingredientId);
        if (ingredient.getState() == 0) {
            ingredient.setState(1);
        } else {
            ingredient.setState(0);
        }
        ingredientRepository.save(ingredient);
    }

    public List<Ingredient> searchByIngredient(String ingredientName) {
        List<Ingredient> searchIngredient = new ArrayList<>();
        //패턴을 컴파일한다.
        Pattern name = Pattern.compile(ingredientName);
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (Ingredient ingredient : findByAll()) {
            Matcher matcher = name.matcher(ingredient.getName());
            if (matcher.find()) {
                searchIngredient.add(ingredient);
            }
        }
        return searchIngredient;
    }
}
