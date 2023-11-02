package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsRegistrationRequestDto;
import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.SupplierRepository;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SupplyDetailsService {

    private final SupplyRepository supplyRepository;

    private final SupplyDetailsRepository supplyDetailsRepository;

    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;

    public List<SupplyDetails> findByAll() {
        return supplyDetailsRepository.findAll();
    }

    public SupplyDetails findBySupplyDetails(Long id) {
        return supplyDetailsRepository.findById(id).orElseThrow();
    }

    public Supply findCreateBySupply(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();
        return supplyRepository.findAll().stream()
                .filter(supply -> supply.getSupplier().equals(supplier) && supply.getState() == 1)
                .findFirst()
                .orElseGet(() -> supplyRepository.save(Supply.builder().supplier(supplier).state(1).build()));
    }

    public void createBySupplyDetails(Long supplierId, Long ingredientId, SupplyDetailsRegistrationRequestDto supplyDetailsRegistrationRequestDto) {
        Supply supply = findCreateBySupply(supplierId);
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow();
        supplyDetailsRepository.findAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getSupplier().getId().equals(supplierId)
                        && supplyDetails.getIngredient().getId().equals(ingredientId)
                        && supplyDetails.getState() == 1)
                .findFirst()
                .ifPresentOrElse(
                        details -> details.update(supplyDetailsRegistrationRequestDto),
                        () -> supplyDetailsRepository.save(SupplyDetails.builder()
                                .supply(supply)
                                .ingredient(ingredient)
                                .price(supplyDetailsRegistrationRequestDto.getPrice())
                                .supplyAmount(supplyDetailsRegistrationRequestDto.getSupplyAmount())
                                .state(1)
                                .build()));
    }

    public void updateBySupplyDetailsAmount(Long supplyDetailId, int updateAmount) {
        SupplyDetails supplyDetails = findBySupplyDetails(supplyDetailId);
        supplyDetails.setSupplyAmount(updateAmount);
        supplyDetailsRepository.save(supplyDetails);
    }

    public void deleteBySupplyDetails(Long id) {
        SupplyDetails supplyDetails = findBySupplyDetails(id);
        if (supplyDetails.getState() == 1) {
            supplyDetailsRepository.delete(supplyDetails);
        }
    }

    //공급 업체별 재료 공급 희망 리스트
    public List<SupplyDetails> findBySupplyDetailAll(Long supplierId) {
        return findByAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getSupplier().getId().equals(supplierId) && supplyDetails.getState() == 1)
                .collect(Collectors.toList());
    }
}
