package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.recipe.RecipeRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.recipe.RecipeSearchRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Recipe;
import com.coffeestore.coffeestore.service.IngredientService;
import com.coffeestore.coffeestore.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;
    @GetMapping
    public String recipeManagement(@RequestParam("menuId")Long menuId, Model model){
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }
    @PostMapping("/recipeSearch")
    public String searchByRecipe(Model model, RecipeSearchRequestDto recipeSearchRequestDto) {
        ingredientService.ingredientSearch(model,recipeSearchRequestDto);
        return "redirect:/recipe";
    }

    @RequestMapping("/backView")
    public String backView(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
    @PostMapping("/addIngredient")
    public String addByIngredient(Model model,@RequestParam("ingredientId")Long ingredientId, RecipeRegistrationRequestDto recipeRegistrationRequestDto){
        Ingredient ingredient = ingredientService.findByIngredient(ingredientId);
        Menu menu = (Menu) model.getAttribute("menu");
        recipeService.createByRecipe(menu.getId(),ingredientId,recipeRegistrationRequestDto);
        return "redirect:/recipe";
    }
}
