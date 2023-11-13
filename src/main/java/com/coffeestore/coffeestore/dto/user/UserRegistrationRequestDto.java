package com.coffeestore.coffeestore.dto.user;

import com.coffeestore.coffeestore.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserRegistrationRequestDto {

    private String name;

    private String address;

    private String phoneNumber;

    public User toEntity(){ //User로 반환
        return User.builder().
                name(name).
                address(address).
                phoneNumber(phoneNumber).
                point(0).
                stamp(0).
                state(1).
                createdDate(new Date()).
                build();
    }
}
