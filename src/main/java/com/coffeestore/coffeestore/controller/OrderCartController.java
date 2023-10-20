package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.service.OrderCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orderCart")
@Slf4j
public class OrderCartController {
    private final OrderCartService orderCartService;
    @GetMapping
    public String orderCarts(Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        model.addAttribute("orderCarts", orderCartService.findByUser(user));
        return "/management/orderCartManagement";
    }
    @PostMapping("/registration")
    public String cartRegistration(@RequestParam("menuId")Long menuId,HttpServletRequest request){
        orderCartService.createByOrderCart(request,menuId);
        return "redirect:/home";
    }
    @PatchMapping("/update")
    public String changeByCartAmount(@RequestParam("cartId")Long cartId,Integer updateAmount){
        orderCartService.updateByOrderCartAmount(cartId,updateAmount);
        return "redirect:/orderCart";
    }
    @DeleteMapping("delete")
    public String deleteByCart(@RequestParam("cartId")Long cartId){
        orderCartService.deleteByOrderCart(cartId);
        return "redirect:/orderCart";
    }
}
