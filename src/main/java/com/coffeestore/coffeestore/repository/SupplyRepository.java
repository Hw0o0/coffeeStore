package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Supplier;
import com.coffeestore.coffeestore.entity.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply,Long> {
    List<Supply> findSupplyBySupplier(Supplier supplier);
    List<Supply> findSupplyBySupplierAndState(Supplier supplier, int state);
}
