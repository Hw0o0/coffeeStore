package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsUpdateDto;
import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.SupplierRepository;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SupplyDetailsService {

    private final SupplyRepository supplyRepository;

    private final SupplyDetailsRepository supplyDetailsRepository;

    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;

    public SupplyDetails findBySupplyDetails(Long id){
        Optional<SupplyDetails> supplyDetails = supplyDetailsRepository.findById(id);
        return supplyDetails.orElse(null);
    }

    //SupplyDetails 생성하거나 찾아서 supply 반환
    public Supply findcreateBySupply(Long supplierId) {
        Optional<Supplier> supplier = supplierRepository.findById(supplierId);
        Supply supplyInfo = supplyRepository.findAll()
                .stream()
                .filter(supply -> supply.getSupplier().equals(supplier) && supply.getState() == 1)
                .findFirst()
                .orElse(null);
        if (supplyInfo == null) {
            Supply supply = new Supply();
            supply.setSupplier(supplier.orElse(null));
            supply.setState(1);
            return supplyRepository.save(supply);
        }else {
            return supplyInfo;
        }
    }

    public void createBySupplyDetails(Long ingredientId,Long supplierId, SupplyDetailsRegistrationRequestDto requestDto) {
        Supply supply = findcreateBySupply(supplierId);
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        if (ingredient.get().getState() == 1) {
            SupplyDetails supplyDetails = requestDto.toEntity(supply, ingredient);
            supplyDetailsRepository.save(supplyDetails);
        }
    }

    public void updateBySupplyDetailsAmount(Long id, SupplyDetailsUpdateDto supplyDetailsUpdateDto){
        SupplyDetails supplyDetails = findBySupplyDetails(id);
        supplyDetails.update(supplyDetailsUpdateDto);
        supplyDetailsRepository.save(supplyDetails);
    }

    public void deleteBySupplyDetails(Long id) {
        SupplyDetails supplyDetails = findBySupplyDetails(id);
        if (supplyDetails.getState() == 1) {
            supplyDetailsRepository.delete(supplyDetails);
        }
    }
}
