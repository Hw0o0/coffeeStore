package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
