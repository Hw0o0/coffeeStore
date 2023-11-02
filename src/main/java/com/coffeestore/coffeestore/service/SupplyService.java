package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplyDetailsRepository supplyDetailsRepository;

    //공급업체별 모든 공급 내역
    public List<Supply> findByAll(){
        return supplyRepository.findAll();
    }
    public Supply findBySupply(Long id){
        Optional<Supply> supply = supplyRepository.findById(id);
        return supply.orElse(null);
    }

    public Supply setTotalPrice(Long supplyId,int totalPrice) {
        Supply supply = findBySupply(supplyId);
        supply.setTotalPrice(totalPrice);
        return supplyRepository.save(supply);
    }

    public void supplyOk(Supply supply, List<SupplyDetails> supplyDetailsList) {
        for (SupplyDetails supplyDetails : supplyDetailsList) {
            supplyDetails.setState(0);
            supplyDetails.getIngredient().setAmount(supplyDetails.getSupplyAmount());
            ingredientRepository.save(supplyDetails.getIngredient());
            supplyDetailsRepository.save(supplyDetails);
        }
        supply.setState(0);
        supply.setDueDate(new Date());
        supplyRepository.save(supply);
    }

    public Supply findByName(String supplierName) {
        return supplyRepository.findBySupplierName(supplierName);
    }
}
