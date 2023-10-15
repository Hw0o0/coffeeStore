package com.coffeestore.coffeestore.dto.order;


import lombok.Data;

import java.util.Date;

@Data
public class OrderRegistrationRequestDto {

    private String paymentMethod;

    private int totalPrice;

}
