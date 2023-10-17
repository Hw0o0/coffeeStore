package com.coffeestore.coffeestore.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequestDto {

    private String updateName;
    
    private String updateAddress;
    
    private Integer updatePhoneNumber;

}