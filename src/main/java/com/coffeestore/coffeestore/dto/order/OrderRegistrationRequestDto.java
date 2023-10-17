package com.coffeestore.coffeestore.dto.order;


import lombok.Data;

@Data
public class OrderRegistrationRequestDto {

    private String paymentMethod;

    private Integer totalPrice;

}
