package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.IngredientRepository;
import com.coffeestore.coffeestore.repository.SupplierRepository;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SupplyDetailsService {

    private final SupplyRepository supplyRepository;

    private final SupplyDetailsRepository supplyDetailsRepository;

    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;

    public List<SupplyDetails> findByAll(){
        return supplyDetailsRepository.findAll();
    }
    public SupplyDetails findBySupplyDetails(Long id){
        Optional<SupplyDetails> supplyDetails = supplyDetailsRepository.findById(id);
        return supplyDetails.orElse(null);
    }

    //SupplyDetails 생성하거나 찾아서 supply 반환
    public Supply findcreateBySupply(Long supplierId) {
        Optional<Supplier> supplier = supplierRepository.findById(supplierId);
        Supply supplyInfo = supplyRepository.findAll()
                .stream()
                .filter(supply -> supply.getSupplier().equals(supplier.get()) && supply.getState() == 1)
                .findFirst()
                .orElse(null);
        if (supplyInfo == null) {
            Supply supply = new Supply();
            supply.setSupplier(supplier.get());
            supply.setState(1);
            return supplyRepository.save(supply);
        } else {
            return supplyInfo;
        }
    }
    public void createBySupplyDetails(Long supplierId, Long ingredientId, int price , int supplyAmount) {
        Supply supply = findcreateBySupply(supplierId);
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        SupplyDetails supplyDetailsCheck = supplyDetailsRepository.findAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getSupplier().getId().equals(supplierId)&&supplyDetails.getIngredient().getId().equals(ingredientId)&&supplyDetails.getState()==1)
                .findFirst()
                .orElse(null);
        if(supplyDetailsCheck == null){
            SupplyDetails supplyDetails = new SupplyDetails();
            supplyDetails.setSupply(supply);
            supplyDetails.setIngredient(ingredient.get());
            supplyDetails.setPrice(price);
            supplyDetails.setSupplyAmount(supplyAmount);
            supplyDetails.setState(1);
            supplyDetailsRepository.save(supplyDetails);
        }else{
            supplyDetailsCheck.setPrice(price);
            supplyDetailsCheck.setSupplyAmount(supplyAmount);
            supplyDetailsRepository.save(supplyDetailsCheck);
        }
    }

    public void updateBySupplyDetailsAmount(Long supplyDetailId,int updateAmount){
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
    public List<SupplyDetails> findBySupplyDetailAll(Long supplierId){
        return findByAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getSupplier().getId().equals(supplierId)&&supplyDetails.getState()==1)
                .collect(Collectors.toList());
    }
}
