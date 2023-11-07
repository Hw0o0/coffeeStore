package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.supplier.SupplierRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.supplier.SupplierUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Supplier;
import com.coffeestore.coffeestore.repository.SupplierRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    private final SupplyRepository supplyRepository;

    public List<Supplier> findByAll() {
        return supplierRepository.findAll();
    }

    public Supplier findBySupplier(Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return supplier.orElse(null);
    }

    public Supplier findByName(String supplierName) {
        return supplierRepository.findByName(supplierName).orElseThrow();
    }

    public void createBySupplier(SupplierRegistrationRequestDto supplierRegistrationDto) {
        Supplier supplier = supplierRegistrationDto.toEntity();
        supplierRepository.save(supplier);
    }

    public void updateBySupplier(Long id, SupplierUpdateRequestDto supplierUpdateRequestDto) {
        Supplier supplier = findBySupplier(id);
        supplier.update(supplierUpdateRequestDto);
        supplierRepository.save(supplier);
    }

    //공급업체 삭제
    public void deleteBySupplier(Long id) {
        Supplier supplier = findBySupplier(id);
        //공급한 내역들 삭제.
        supplyRepository.findSupplyBySupplier(supplier)
                .forEach(supply -> {
                    supply.setState(0);
                    supplyRepository.save(supply);
                });
        supplier.setState(0);
        supplierRepository.save(supplier);
    }

    public void stateChangeBySupplier(Long supplierId) {
        Supplier supplier = findBySupplier(supplierId);
        if (supplier.getState() == 0) {
            supplier.setState(1);
        } else {
            supplier.setState(0);
        }
        supplierRepository.save(supplier);
    }

    public List<Supplier> searchBySupplier(String supplierName) {
        List<Supplier> suppliers = new ArrayList<>();
        //패턴을 컴파일한다.
        Pattern name = Pattern.compile(supplierName);
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (Supplier supplier : findByAll()) {
            Matcher matcher = name.matcher(supplier.getName());
            if (matcher.find()) {
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }
}
