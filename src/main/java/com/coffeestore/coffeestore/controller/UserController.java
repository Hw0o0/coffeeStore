package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.user.LoginReqDto;
import com.coffeestore.coffeestore.dto.user.UserRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.user.UserUpdateRequestDto;
import com.coffeestore.coffeestore.entity.AddPoint;
import com.coffeestore.coffeestore.entity.UsedPoint;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String userManagement(Model model) {
        model.addAttribute("users", userService.findByAll());
        return "/management/userManagement";
    }

    @GetMapping("/userSearch")
    public String userSearch(Model model, @RequestParam("userName") String userName) {
        List<User> users = userService.searchByUser(userName);
        model.addAttribute("users", users);
        return "/management/userManagement";
    }

    @PostMapping("/login")
    public String login(LoginReqDto loginReqDto, HttpSession session) {
        boolean checked = userService.login(loginReqDto);
        User user = userService.findByNameAndPhoneNumber(loginReqDto);
        if (checked) {
            //session 유효 시간 60분 설정
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(3600);
            return "redirect:/home";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/registration")
    public String registration(UserRegistrationRequestDto userRegistrationRequestDto) {
        userService.createByUser(userRegistrationRequestDto);
        return "redirect:/home";
    }

    @PatchMapping("/state")
    public String userState(@RequestParam("userId") Long userId) {
        userService.stateChangeByUser(userId);
        return "redirect:/user";
    }

    @PatchMapping("/update")
    public String userUpdate(@RequestParam("userId") Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        userService.updateByUser(userId, userUpdateRequestDto);
        return "redirect:/user";
    }
    @PatchMapping("/userEdit")
    public String userEdit(@RequestParam("userId") Long userId,Model model, UserUpdateRequestDto userUpdateRequestDto){
        User user = userService.updateByUser(userId, userUpdateRequestDto);
        model.addAttribute(user);
        return "myPage";
    }

    @DeleteMapping("/delete")
    public String userDelete(@RequestParam("userId") Long userId) {
        userService.deleteByUser(userId);
        return "redirect:/user";
    }
    @GetMapping("/addPointList")
    public String addPointList(HttpServletRequest request,Model model){
        User user = userService.findBySessionUser(request);
        List<AddPoint> addPointList = userService.findByAddPointList(user);
        model.addAttribute(user).addAttribute(addPointList);
        return "addPointList";
    }
    @GetMapping("/usedPointList")
    public String usedPointList(HttpServletRequest request,Model model){
        User user = userService.findBySessionUser(request);
        List<UsedPoint> usedPointList = userService.findByUsedPointList(user);
        model.addAttribute(user).addAttribute(usedPointList);
        return "usedPointList";
    }
}
