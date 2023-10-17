package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.supplier.SupplierUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    public void update(SupplierUpdateRequestDto supplierUpdateRequestDto){
        this.name = supplierUpdateRequestDto.getUpdateName();
        this.address = supplierUpdateRequestDto.getUpdateAddress();
        this.modificationDate = new Date();
    }
}
