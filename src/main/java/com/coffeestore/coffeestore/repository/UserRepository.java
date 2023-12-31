package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);

    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);

    Boolean existsByPhoneNumber(String phoneNumber);
}
