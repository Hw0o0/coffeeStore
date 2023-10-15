package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
import com.coffeestore.coffeestore.dto.order.OrderRegistrationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String categorize;

    @Column(nullable = false)
    private int state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    public void update(MenuUpdateRequestDto menuUpdateRequestDto){
        this.name = menuUpdateRequestDto.getName();
        this.price = menuUpdateRequestDto.getPrice();
        this.categorize = menuUpdateRequestDto.getCategorize();
        this.modificationDate = new Date();
        this.state = 1;
    }
}
