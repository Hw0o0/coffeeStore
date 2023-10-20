package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.repository.OrderRepository;
import com.coffeestore.coffeestore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping
    public String orderView(Model model, @RequestParam("orderCartList") List<OrderCart> orderCartList) {
        Order order = orderService.findByOrder(orderCartList.get(0).getOrder().getId());
        //주문 금액 계산
        int totalPrice = orderCartList.stream()
                .mapToInt(orderCart -> orderCart.getMenu().getPrice() * orderCart.getAmount())
                .sum();
        // 총 주문금액 설정
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        //Order order = orderService.setTotalPrice(orderCartList);
        model.addAttribute("orderCartList", orderCartList);
        model.addAttribute("order",order);
        return "/management/orderManagement";
    }

}
