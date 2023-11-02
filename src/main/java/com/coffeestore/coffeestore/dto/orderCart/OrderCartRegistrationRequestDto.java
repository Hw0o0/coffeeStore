package com.coffeestore.coffeestore.dto.orderCart;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import lombok.Data;


@Data
public class OrderCartRegistrationRequestDto {
        private Integer amount;

        public OrderCart toEntity(Order order, Menu menu){
                return OrderCart.builder()
                        .order(order)
                        .menu(menu)
                        .amount(amount)
                        .state(1)
                        .build();
        }
}
