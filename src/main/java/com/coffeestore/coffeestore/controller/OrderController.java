package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.service.OrderCartService;
import com.coffeestore.coffeestore.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderCartService orderCartService;

    @GetMapping
    public String findByAllOrder(Model model, HttpServletRequest request) {
        List<Order> orderList = orderService.findAll(request);
        model.addAttribute(orderList);
        return "management/orderManagement";
    }

    @GetMapping("/orderUserSearch")
    public String orderUserSearch(Model model, @RequestParam("userName") String userName) {
        List<Order> orderList = orderService.findByOrderUsers(userName);
        model.addAttribute("orderList", orderList);
        return "/management/orderManagement";
    }

    //orderCartManagement 에서 여러개 주문 버튼 누를시
    @GetMapping("/orderPage")
    public String orderView(Model model, HttpServletRequest request) {
        Order order = orderService.findByOrder(request);
        List<OrderCart> orderCartList = orderService.createByOrderPageView(order);
        model.addAttribute(order).addAttribute(orderCartList);
        return "orderPage";
    }

    //menuDetailsInfo 에서 하나만 구매 버튼 누를시
    @GetMapping("/purchase")
    public String purchaseMenu(Model model, @RequestParam("menuId") Long menuId,int amount, HttpServletRequest request) {
        OrderCart orderCart = orderCartService.createByOneOrderCart(request, menuId,amount);
        Order order = orderService.findByOrderAndSetPrice(orderCart.getOrder().getId(), orderCart);
        List<OrderCart> orderCartList = new ArrayList<>();
        orderCartList.add(orderCart);
        model.addAttribute(order).addAttribute(orderCartList);
        return "orderPage";
    }

    @PostMapping("/menuOrder")
    public String orderOk(@RequestParam("orderId") Long orderId, String payMethod,Integer usedPoint) {
        orderService.orderOk(payMethod, orderId, usedPoint);
        return "redirect:/home";
    }
}