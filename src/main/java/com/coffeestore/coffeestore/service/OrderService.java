package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.OrderCartRepository;
import com.coffeestore.coffeestore.repository.OrderRepository;
import com.coffeestore.coffeestore.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderCartRepository orderCartRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;


    // 자신의 주문만 볼수 있게끔 함.
    public List<Order> findAll(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String manager = "1";
        if (manager.equals(user.getName())) {
            return orderRepository.findAll();
        } else {
            return orderRepository.findAll()
                    .stream()
                    .filter(order -> order.getUser().equals(user))
                    .collect(Collectors.toList());
        }
    }

    public Order findByOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    //orderView를 위한 메소드
    public List<OrderCart> createByOrderPage(Order order) {
        List<OrderCart> orderCartList = orderCartRepository.findAll().stream()
                .filter(orderCart -> orderCart.getOrder().getId().equals(Objects.requireNonNull(order).getId()))
                .collect(Collectors.toList());
        //주문 금액 계산
        int totalPrice = orderCartList.stream()
                .mapToInt(orderCart -> orderCart.getMenu().getPrice() * orderCart.getAmount())
                .sum();
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        return orderCartList;
    }

    //메뉴에 사용된 재료로 인한 재료 수량 수정 메소드
    public void menuUsedRecipeMinus(OrderCart orderCart) {
        recipeRepository.findAll()
                .stream()
                .filter(recipe -> recipe.getMenu().getId().equals(orderCart.getMenu().getId()))
                .forEach(recipe -> {
                    int usedRecipe = recipe.getAmount() * orderCart.getAmount();
                    Ingredient ingredient = recipe.getIngredient();
                    ingredient.setAmount(recipe.getIngredient().getAmount() - usedRecipe);
                    ingredientRepository.save(ingredient);
                });
    }

    //주문
    public Order findByOrderCart(User user) {
        return orderRepository.findAll()
                .stream()
                .filter(orders -> orders.getUser().equals(user) && orders.getState() == 1)
                .findFirst()
                .orElse(null);
    }

    //주문 완료 시 시작하는 메소드
    public void orderOk(String payMethod, Long orderId) {
        Order orderInfo = findByOrder(orderId);

        List<OrderCart> orderCartList = orderCartRepository.findAll()
                .stream()
                .filter(orderCartInfo -> orderCartInfo.getOrder().equals(orderInfo))
                .collect(Collectors.toList());
        OrderCart orderCart = orderCartList
                .stream()
                .filter(orderCart1 -> orderCart1.getState()==2)
                .findFirst()
                .orElse(null);

        // 메뉴 하나 주문할 떄 상황
        if(orderCart == null) {
            orderInfo.setPaymentMethod(payMethod);
            orderInfo.setState(0);
            orderInfo.setCreatedDate(new Date());
            orderRepository.save(orderInfo);

            orderCartList.forEach(orderCart2 -> {
                menuUsedRecipeMinus(orderCart2);
                orderCart2.setState(0);
                orderCartRepository.save(orderCart2);
            });
        // 메뉴 여러개 주문할 떄 상황
        }else{
            orderCartRepository.delete(orderCart);
            Order order = Order.builder()
                    .user(orderInfo.getUser())
                    .paymentMethod(payMethod)
                    .totalPrice(orderCart.getAmount()*orderCart.getMenu().getPrice())
                    .state(0)
                    .createdDate(new Date())
                    .build();
            orderRepository.save(order);
            orderCart.setOrder(order);
            orderCart.setState(0);
            orderCartRepository.save(orderCart);
        }
    }

    public Order findByOrderAndSetPrice(Long orderId, OrderCart orderCart) {
        Order order = findByOrder(orderId);
        order.setTotalPrice(orderCart.getAmount() * orderCart.getMenu().getPrice());
        orderRepository.save(order);
        orderCart.setState(2);
        orderCartRepository.save(orderCart);
        return order;
    }

    public List<Order> findByOrderUsers(String userName) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getUser().getName().equals(userName))
                .collect(Collectors.toList());
    }
}