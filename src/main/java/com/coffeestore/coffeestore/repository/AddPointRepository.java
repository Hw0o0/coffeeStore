package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.AddPoint;
import com.coffeestore.coffeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddPointRepository extends JpaRepository<AddPoint,Long> {
    List<AddPoint> findAddPointByUser(User user);
}
