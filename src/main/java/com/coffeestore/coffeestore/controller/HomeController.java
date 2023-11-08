package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.service.MenuService;
import com.coffeestore.coffeestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final MenuService menuService;
    private final UserService userService;

    @GetMapping
    public String homeView(Model model) {
        List<Menu> menuList = menuService.homeView();
        model.addAttribute("menus", menuList);
        return "/home";
    }
    @GetMapping("/menuSearch")
    public String menuSearch(Model model, @RequestParam("menuName") String menuName) {
        List<Menu> menus = menuService.searchByMenu(menuName);
        model.addAttribute("menus", menus);
        return "/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/userRegistration")
    public String register() {
        return "userRegistration";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        menuService.sessionInvalidate(request);
        return "redirect:/home";
    }
    @GetMapping("/myPage")
    public String myPageView(Model model, HttpServletRequest request){
        User user = userService.findBySessionUser(request);
        model.addAttribute(user);
        return "myPage";
    }
}
