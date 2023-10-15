package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.user.UserRegistrationRequestDto;
import com.coffeestore.coffeestore.service.MenuService;
import com.coffeestore.coffeestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final MenuService menuItemService;

//    @GetMapping
//    public String home(Model model){
//        model.addAttribute("coffee", menuItemService.findByAll());
//        return "redirect:/home";
//    }
    @GetMapping("/register")
    public String register(){
        return "userRegistration";
    }
    @PostMapping("/registration")
    public String registration(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto){
        userService.createByUser(userRegistrationRequestDto);
        return "redirect:/home";
    }
}
