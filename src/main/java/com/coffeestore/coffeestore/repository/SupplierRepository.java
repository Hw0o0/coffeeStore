package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
