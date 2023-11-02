package com.coffeestore.coffeestore.dto.supplyDetails;

import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Supply;
import com.coffeestore.coffeestore.entity.SupplyDetails;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SupplyDetailsRegistrationRequestDto {
    private Integer supplyAmount;
    private Integer price;

    public SupplyDetails toEntity(Supply supply, Ingredient ingredient){
        return  SupplyDetails.builder()
                .supply(supply)
                .ingredient(ingredient)
                .price(price)
                .supplyAmount(supplyAmount)
                .state(1)
                .build();
    }
}
