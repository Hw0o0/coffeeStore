package com.coffeestore.coffeestore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @Builder
    public Supply(Supplier supplier, int state) {
        this.supplier = supplier;
        this.state = state;
    }
}
