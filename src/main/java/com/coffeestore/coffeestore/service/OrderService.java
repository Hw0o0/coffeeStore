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
import java.util.Optional;
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
        } else {return orderRepository.findAll()
                    .stream()
                    .filter(order -> order.getUser().equals(user))
                    .collect(Collectors.toList());
        }
    }

    public Order findByOrder(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
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
    public void menuUsedRecipeMinus(OrderCart orderCart){
        List<Recipe> recipes = recipeRepository.findAll()
                .stream()
                .filter(recipe -> recipe.getMenu().getId().equals(orderCart.getMenu().getId()))
                .collect(Collectors.toList());
        for(Recipe recipe : recipes){
            //요리에 사용된 재료 사용량 재료 수량 수정해 줌
            int usedRecipe = recipe.getAmount()*orderCart.getAmount();
            Ingredient ingredient = recipe.getIngredient();
            ingredient.setAmount(recipe.getIngredient().getAmount()-usedRecipe);
            ingredientRepository.save(ingredient);
        }
    }

    //주문
    public Order findByOrderCart(User user){
        return  orderRepository.findAll()
                .stream()
                .filter(orders -> orders.getUser().equals(user)&&orders.getState()==1)
                .findFirst()
                .orElse(null);
    }

    //주문 완료 시 시작하는 메소드
    public void orderOk(String payMethod, Long orderId) {
        Order order = findByOrder(orderId);
        if(order.getTotalPrice() == 0){
            orderRepository.delete(order);
        }
        List<OrderCart> orderCartList = orderCartRepository.findAll()
                .stream()
                .filter(orderCart -> orderCart.getOrder().getId().equals(order.getId()))
                .collect(Collectors.toList());
        if(!orderCartList.isEmpty()){
            order.setPaymentMethod(payMethod);
            order.setState(0);
            order.setCreatedDate(new Date());
            orderRepository.save(order);

            for(OrderCart orderCart : orderCartList){
                menuUsedRecipeMinus(orderCart);
                orderCart.setState(0);
                orderCartRepository.save(orderCart);
            }
        }
    }

    public Order findByOrderAndSetPrice(Long orderId, OrderCart orderCart) {
        Order order = findByOrder(orderId);
        order.setTotalPrice(orderCart.getAmount()*orderCart.getMenu().getPrice());
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