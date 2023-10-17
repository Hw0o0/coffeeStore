package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
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
    private Integer state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    public void update(MenuUpdateRequestDto menuUpdateRequestDto){
        this.name = menuUpdateRequestDto.getUpdateName();
        this.price = menuUpdateRequestDto.getUpdatePrice();
        this.categorize = menuUpdateRequestDto.getUpdateCategorize();
        this.modificationDate = new Date();
        this.state = 1;
    }
}
