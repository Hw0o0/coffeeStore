package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.ingredient.IngredientRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.ingredient.IngredientUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public String ingredientManagement(Model model){
        model.addAttribute("ingredients", ingredientService.findByAll());
        return "/management/ingredientManagement";
    }
    @GetMapping("/ingredientSearch")
    public String ingredientSearch(Model model,@RequestParam("ingredientName")String ingredientName){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredientService.findByName(ingredientName));
        model.addAttribute("ingredients",ingredients);
        return "/management/ingredientManagement";
    }

    @PostMapping("/registration")
    public String registration(IngredientRegistrationRequestDto ingredientRegistrationRequestDto){
        ingredientService.createByIngredient(ingredientRegistrationRequestDto);
        return "redirect:/ingredient";
    }

    @PatchMapping("/state")
    public String userState(@RequestParam("ingredientId") Long ingredientId){
        ingredientService.stateChangeByIngredient(ingredientId);
        return"redirect:/ingredient";
    }
    @PatchMapping("/update")
    public String userUpdate(@RequestParam("ingredientId") Long ingredientId, IngredientUpdateRequestDto ingredientUpdateRequestDto){
        ingredientService.updateByIngredient(ingredientId,ingredientUpdateRequestDto);
        return "redirect:/ingredient";
    }
    @DeleteMapping("/delete")
    public String userDelete(@RequestParam("ingredientId") Long ingredientId){
        ingredientService.deleteByIngredient(ingredientId);
        return "redirect:/ingredient";
    }
}
