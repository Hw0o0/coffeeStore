package com.coffeestore.coffeestore.entity;

import com.coffeestore.coffeestore.dto.user.UserUpdateRequestDto;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false,name = "phone_number")
    private Integer phoneNumber;

    @Column(nullable = false)
    private int state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modification_date")
    private Date modificationDate;

    public void update(UserUpdateRequestDto userUpdateRequestDto){
        this.name = userUpdateRequestDto.getUpdateName();
        this.address = userUpdateRequestDto.getUpdateAddress();
        this.phoneNumber = userUpdateRequestDto.getUpdatePhoneNumber();
        this.modificationDate = new Date();
    }

}
