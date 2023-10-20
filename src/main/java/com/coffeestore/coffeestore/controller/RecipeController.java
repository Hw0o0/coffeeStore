package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.recipe.RecipeSearchRequestDto;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final MenuService menuService;

    @GetMapping
    public String recipeManagement(@RequestParam("menuId")Long menuId, Model model){
        model.addAttribute("menuId",menuId);
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }
    @GetMapping("/ingredientSearch")
    public String ingredientSearch(Model model, RecipeSearchRequestDto recipeSearchRequestDto, @RequestParam("menuId")Long menuId){
        Optional<Ingredient> ingredient = ingredientService.ingredientSearch(recipeSearchRequestDto);
        model.addAttribute("ingredient", ingredient.get());
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        model.addAttribute("menuId",menuId);
        return "/management/recipeManagement";
    }

    @RequestMapping("/backView")
    public String backView(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
    @GetMapping("/addIngredient")
    public String addByIngredient(Model model,Integer amount,@RequestParam("ingredientId")Long ingredientId,@RequestParam("menuId")Long menuId){
        Ingredient ingredient = ingredientService.findByIngredient(ingredientId);
        Menu menu = menuService.findByMenu(menuId);
        recipeService.createByRecipe(menu,ingredient,amount);
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        model.addAttribute("menuId",menuId);
        model.addAttribute("ingredient",new Ingredient());
        return "/management/recipeManagement";
    }
    @PatchMapping("/update")
    public String updateByRecipe(Model model,@RequestParam("recipeId")Long recipeId,@RequestParam("menuId")Long menuId, RecipeUpdateRequestDto recipeUpdateRequestDto){
        recipeService.updateByRecipe(recipeId,recipeUpdateRequestDto);
        model.addAttribute("menuId",menuId);
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }
    @DeleteMapping("/delete")
    public String deleteByRecipe(Model model,@RequestParam("recipeId")Long recipeId,@RequestParam("menuId")Long menuId){
        recipeService.deleteByRecipe(recipeId);
        model.addAttribute("menuId",menuId);
        model.addAttribute("recipes", recipeService.findByMenuUseAll(menuId));
        return "/management/recipeManagement";
    }
}
