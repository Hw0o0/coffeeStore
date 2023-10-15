package com.coffeestore.coffeestore.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Supplier supplier;

    @Column(nullable = false)
    private int totalPrice;

    @Column(name = "due_date") //납기일
    private Date dueDate;

    @Column(nullable = false)
    private int state;

    //0-공급취소,1-공급 주문중,2-공급완료
    public void registration(int totalPrice){
        this.totalPrice = totalPrice;
        this.dueDate = new Date();
        this.state = 2;
    }
}
