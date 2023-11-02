package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.recipe.RecipeUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.service.IngredientService;
import com.coffeestore.coffeestore.service.MenuService;
import com.coffeestore.coffeestore.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final MenuService menuService;

    @GetMapping
    public String recipeManagement(Model model, @RequestParam("menuId") Long menuId) {
        model.addAttribute("menuId", menuId).addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }

    @GetMapping("/ingredientSearch")
    public String ingredientSearch(Model model, @RequestParam("menuId") Long menuId, String ingredientName) {
        List<Ingredient> ingredientList = ingredientService.searchByIngredient(ingredientName);
        model.addAttribute("ingredientList", ingredientList).addAttribute("recipes", recipeService.findByMenuUseAll(menuId)).addAttribute("menuId", menuId);
        return "/management/recipeManagement";
    }

    @GetMapping("/addIngredient")
    public String addByIngredient(Model model, @RequestParam("ingredientId") Long ingredientId, @RequestParam("menuId") Long menuId, Integer amount) {
        Ingredient ingredient = ingredientService.findByIngredient(ingredientId);
        Menu menu = menuService.findByMenu(menuId);
        recipeService.createByRecipe(menu, ingredient, amount);
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId)).addAttribute("menuId", menuId).addAttribute("ingredient", new Ingredient());
        return "/management/recipeManagement";
    }

    @PatchMapping("/update")
    public String updateByRecipe(Model model, @RequestParam("recipeId") Long recipeId, @RequestParam("menuId") Long menuId, RecipeUpdateRequestDto recipeUpdateRequestDto) {
        recipeService.updateByRecipe(recipeId, recipeUpdateRequestDto);
        model.addAttribute("menuId", menuId).addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }

    @DeleteMapping("/delete")
    public String deleteByRecipe(Model model, @RequestParam("recipeId") Long recipeId, @RequestParam("menuId") Long menuId) {
        recipeService.deleteByRecipe(recipeId);
        model.addAttribute("menuId", menuId).addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }
}
