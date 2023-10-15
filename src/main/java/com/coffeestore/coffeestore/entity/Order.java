package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.order.OrderRegistrationRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(nullable = false)
    private int state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    //0-주문취소,1-주문 진행중,2-주문완료
    public void registration(OrderRegistrationRequestDto orderRegistrationRequestDto){
        this.paymentMethod = orderRegistrationRequestDto.getPaymentMethod();
        this.totalPrice = orderRegistrationRequestDto.getTotalPrice();
        this.createdDate = new Date();
        this.state = 2;
    }
}
