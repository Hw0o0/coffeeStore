package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.menu.MenuRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    @GetMapping
    public String menuManagement(Model model){
        model.addAttribute("menus",menuService.findByAll());
        return "/management/menuManagement";
    }
    @GetMapping("/menuSearch")
    public String menuSearch(Model model,@RequestParam("menuName")String menuName){
        List<Menu> menus = new ArrayList<>();
        menus.add(menuService.findByName(menuName));
        model.addAttribute("menus",menus);
        return "/management/menuManagement";
    }
    @PostMapping("/registration")
    public String menuRegistration(MenuRegistrationRequestDto menuRegistrationRequestDto){
        menuService.createByMenu(menuRegistrationRequestDto);
        return "redirect:/menu";
    }
    @PatchMapping("/state")
    public String menuState(@RequestParam("menuId") Long menuId){
        menuService.stateChangeByMenu(menuId);
        return"redirect:/menu";
    }
    @PatchMapping("/update")
    public String menuUpdate(@RequestParam("menuId") Long menuId,MenuUpdateRequestDto menuUpdateRequestDto){
        menuService.updateByMenu(menuId,menuUpdateRequestDto);
        return "redirect:/menu";
    }
    @DeleteMapping("/delete")
    public String menuDelete(@RequestParam("menuId") Long menuId){
        menuService.deleteByMenu(menuId);
        return "redirect:/menu";
    }
}
