package com.coffeestore.coffeestore.dto.menu;

import com.coffeestore.coffeestore.entity.Menu;
import lombok.Data;

import java.util.Date;

@Data
public class MenuRegistrationRequestDto {

    private String name;

    private String price;

    private String categorize;

    public Menu toEntity(){
        return Menu.builder()   //menu 반환
                .name(name)
                .price(Integer.parseInt(price))
                .categorize(categorize)
                .state(1)
                .createdDate(new Date())
                .build();
    }
}
