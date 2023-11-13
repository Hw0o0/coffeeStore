package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.UsedPoint;
import com.coffeestore.coffeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsedPointRepository extends JpaRepository<UsedPoint,Long> {
    List<UsedPoint> findUsedPointByUser(User user);
}
