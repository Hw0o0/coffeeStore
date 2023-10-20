package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderCartRepository extends JpaRepository<OrderCart,Long> {
    Optional<OrderCart> findByMenuAndOrder_UserAndState(Menu menu, User user, int state);
}
