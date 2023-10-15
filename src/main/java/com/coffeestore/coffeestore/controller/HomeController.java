package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.user.LoginReqDto;
import com.coffeestore.coffeestore.service.MenuService;
import com.coffeestore.coffeestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final MenuService menuService;

    @GetMapping
    public String homeView(Model model){
        model.addAttribute("menu",menuService.findByAll());
        return "/home";
    }
    @GetMapping("/login")
    public String login(@RequestParam("loginReqDto") LoginReqDto loginReqDto){
        boolean checked = userService.login(loginReqDto);
        if(checked){
            return "redirect:/home";
        }else {
            return "/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/home";
    }
}
