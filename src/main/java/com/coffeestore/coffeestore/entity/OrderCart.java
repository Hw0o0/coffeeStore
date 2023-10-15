package com.coffeestore.coffeestore.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Menu menu;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int state;

}
