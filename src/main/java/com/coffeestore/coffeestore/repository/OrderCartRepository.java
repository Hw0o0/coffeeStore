package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCartRepository extends JpaRepository<OrderCart,Long> {
}
