package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    Optional<Menu> findByName(String name);
}
