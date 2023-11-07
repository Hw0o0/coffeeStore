package com.coffeestore.coffeestore.repository;

import com.coffeestore.coffeestore.entity.Supply;
import com.coffeestore.coffeestore.entity.SupplyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplyDetailsRepository extends JpaRepository<SupplyDetails,Long> {
    List<SupplyDetails> findSupplyDetailsBySupply(Supply supply);

    @Query("SELECT sd FROM SupplyDetails sd WHERE sd.supply.supplier.id = :supplierId " +
            "AND sd.ingredient.id = :ingredientId AND sd.state = 1")
    List<SupplyDetails> findSupplyDetailsBySupplierIdAndIngredientId(Long supplierId, Long ingredientId);
}
