package com.coffeestore.coffeestore.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequestDto {

    private String name;
    
    private String address;
    
    private String phoneNumber;

}