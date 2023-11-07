package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findOrderByUser_IdAndState(Long userId, int state);
    List<Order> findOrderByUserAndState(User user,int state);
    List<Order> findOrderByState(int State);
    List<Order> findOrderByUserName(String user_name);
}
