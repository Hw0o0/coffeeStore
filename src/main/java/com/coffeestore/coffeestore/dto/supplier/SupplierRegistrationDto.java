package com.coffeestore.coffeestore.dto.supplier;

import com.coffeestore.coffeestore.entity.Supplier;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SupplierRegistrationDto {
    private String name;

    private String address;

    public Supplier toEntity(){
        return Supplier.builder()
                .name(name)
                .address(address)
                .state(1)
                .createdDate(new Date())
                .build();
    }
}
