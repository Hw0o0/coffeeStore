package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.supplier.SupplierRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.supplier.SupplierUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Supplier;
import com.coffeestore.coffeestore.entity.Supply;
import com.coffeestore.coffeestore.repository.SupplierRepository;
import com.coffeestore.coffeestore.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    private final SupplyRepository supplyRepository;

    public List<Supplier> findByAll(){
        return supplierRepository.findAll();
    }
    public Supplier findBySupplier(Long id){
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return supplier.orElse(null);
    }
    public Supplier findByName(String supplierName) {
        Optional<Supplier> supplier = supplierRepository.findByName(supplierName);
        return supplier.get();
    }

    public void createBySupplier(SupplierRegistrationRequestDto supplierRegistrationDto) {
        Supplier supplier = supplierRegistrationDto.toEntity();
        supplierRepository.save(supplier);
    }
    public void updateBySupplier(Long id, SupplierUpdateRequestDto supplierUpdateRequestDto){
        Supplier supplier = findBySupplier(id);
        supplier.update(supplierUpdateRequestDto);
        supplierRepository.save(supplier);
    }

    //공급업체 삭제
    public void deleteBySupplier(Long id){
        Supplier supplier = findBySupplier(id);

        //공급한 내역들 삭제.
        List<Supply> supplies = supplyRepository.findAll()
                .stream()
                .filter(Supply -> Supply.getSupplier().getId().equals(id))
                .collect(Collectors.toList());
        for(Supply supply : supplies){
            supply.setState(0);
        }
        supplyRepository.saveAll(supplies);
        supplier.setState(0);
        supplierRepository.save(supplier);
    }

    public void stateChangeBySupplier(Long supplierId) {
        Supplier supplier = findBySupplier(supplierId);
        if(supplier.getState() == 0){
            supplier.setState(1);
        }else {
            supplier.setState(0);
        }
        supplierRepository.save(supplier);
    }


}
