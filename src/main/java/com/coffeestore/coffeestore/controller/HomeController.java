package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final MenuService menuService;

    @GetMapping
    public String homeView(Model model) {
        List<Menu> menuList = menuService.homeView();
        model.addAttribute("menus", menuList);
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
        HttpSession session = request.getSession(false);
        menuService.sessionInvalidate(session);
        return "redirect:/home";
    }
}
