package com.coffeestore.coffeestore.dto.orderCart;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import lombok.Data;

import java.util.Optional;

@Data
public class OrderCartRegistrationRequestDto {
        private int amount;

        public OrderCart toEntity(Order order, Optional<Menu> menu){
                return OrderCart.builder()
                        .order(order)
                        .menu(menu.orElse(null))
                        .amount(amount)
                        .state(1)
                        .build();
        }
}
