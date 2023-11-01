package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;


    private final SupplyDetailsRepository supplyDetailsRepository;

    //공급업체별 모든 공급 내역
    public List<Supply> findByAll(){
        return supplyRepository.findAll();
    }
    public Supply findBySupply(Long id){
        Optional<Supply> supply = supplyRepository.findById(id);
        return supply.orElse(null);
    }
    public void  createBySupply(Long id) {
        Optional<Supply> supply = supplyRepository.findById(id);

        //공급 내역 리스트
        List<SupplyDetails> supplyDetailsList = supplyDetailsRepository.findAll()
                .stream()
                .filter(SupplyDetails -> SupplyDetails.getSupply().getId().equals(id))
                .collect(Collectors.toList());

        //공급된 재료 수만큼 재료 수량 늘려줌
        int total = 0;
        for (SupplyDetails supplyDetails : supplyDetailsList){
            total = supplyDetails.getSupplyAmount()*supplyDetails.getPrice();
            supplyDetails.getIngredient().setAmount(supplyDetails.getSupplyAmount()+supplyDetails.getIngredient().getAmount());
            supplyDetailsRepository.save(supplyDetails);
        }

        if (supply.isPresent()) {
            supply.get().registration(total);
            supplyRepository.save(supply.get());
        }

        // 공급완료 상태로 변경
        for (SupplyDetails supplyDetails : supplyDetailsList) {
            supplyDetails.setState(0);
            supplyDetailsRepository.save(supplyDetails);
        }
    }
    public void deleteBySupply(Long id){
        Supply supply = findBySupply(id);
        supply.setState(0);
        supplyRepository.save(supply);
    }

    public void save(Supply supply) {
        supplyRepository.save(supply);
    }

    public void supplyOk(Supply supply, List<SupplyDetails> supplyDetailsList) {
        for(SupplyDetails supplyDetails : supplyDetailsList){
            supplyDetails.setState(0);
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
