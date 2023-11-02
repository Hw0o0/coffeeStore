package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Supply supply;

    @ManyToOne
    private Ingredient ingredient;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, name = "supply_amount")
    private int supplyAmount;

    @Column(nullable = false)
    private int state;

    public void update(SupplyDetailsUpdateDto supplyDetailsUpdateDto) {
        this.price = supplyDetailsUpdateDto.getPrice();
        this.supplyAmount = supplyDetailsUpdateDto.getSupplyAmount();
    }

    public void update(SupplyDetailsRegistrationRequestDto dto) {
        this.price = dto.getPrice();
        this.supplyAmount = dto.getSupplyAmount();
    }

}
