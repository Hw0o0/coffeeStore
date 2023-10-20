package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.order.OrderRegistrationRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.entity.Recipe;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.OrderCartRepository;
import com.coffeestore.coffeestore.repository.OrderRepository;
import com.coffeestore.coffeestore.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderCartRepository orderCartRepository;

    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order findByOrder(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    //주문하기 버튼을 눌렀을때 작동
    public void createByOrder(Long id, OrderRegistrationRequestDto orderRegistrationRequestDto) {
        //주문하지않은 상태인 cart
        List<OrderCart> orderCarts = orderCartRepository.findAll()
                .stream()
                .filter(orderCart -> orderCart.getOrder().getUser().getId().equals(id) && orderCart.getState() == 1 && orderCart.getMenu().getState() == 1)
                .collect(Collectors.toList());

        // 총 가격 계산
        int totalPrice = orderCarts.stream()
                .mapToInt(OrderCart::getAmount)
                .sum();

        //주문 ID 찾기
        Long orderId = orderCarts.get(0).getId();
        Optional<Order> order = orderRepository.findById(orderId);

        //Order 완성해서 DB 저장
        if(order.isPresent()) {
            order.get().registration(orderRegistrationRequestDto);
            orderRepository.save(order.get());
        }
        //cart 주문완료 상태로 변경
        for (OrderCart cart : orderCarts) {
            cart.setState(0);
            menuUsedRecipeMinus(cart);
        }
        orderCartRepository.saveAll(orderCarts);
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

    //order 삭제
    public void deleteByOrder(Long id){
        Order order = findByOrder(id);
        order.setState(0);
        orderRepository.save(order);
    }
}
