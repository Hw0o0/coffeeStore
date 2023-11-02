package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.SupplyDetailsRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplyService {

    private final SupplyRepository supplyRepository;


    private final SupplyDetailsRepository supplyDetailsRepository;

    //공급업체별 모든 공급 내역
    public List<Supply> findByAll() {
        return supplyRepository.findAll();
    }

    public Supply findBySupply(Long id) {
        Optional<Supply> supply = supplyRepository.findById(id);
        return supply.orElse(null);
    }

    public Supply setTotalPrice(Long supplyId, int totalPrice) {
        Supply supply = findBySupply(supplyId);
        supply.setTotalPrice(totalPrice);
        return supplyRepository.save(supply);
    }

    public void supplyOk(Supply supply, List<SupplyDetails> supplyDetailsList) {
        for (SupplyDetails supplyDetails : supplyDetailsList) {
            supplyDetails.setState(0);
            supplyDetailsRepository.save(supplyDetails);
        }
        supply.setState(0);
        supply.setDueDate(new Date());
        supplyRepository.save(supply);
    }

    public int createByTotalPrice(List<SupplyDetails> supplyDetailsList) {
        return supplyDetailsList.stream()
                .mapToInt(supplyDetails -> supplyDetails.getPrice() * supplyDetails.getSupplyAmount())
                .sum();
    }

    public List<SupplyDetails> findBySupplyDetailsList(Long supplyId) {
        return supplyDetailsRepository.findAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getId().equals(supplyId))
                .collect(Collectors.toList());
    }

    public List<Supply> searchBySupplierName(String supplierName) {
        List<Supply> supplyList = new ArrayList<>();

        Pattern name = Pattern.compile(supplierName);
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (Supply supply : findByAll()) {
            Matcher matcher = name.matcher(supply.getSupplier().getName());
            if (matcher.find()) {
                supplyList.add(supply);
            }
        }
        return supplyList;
    }
}
