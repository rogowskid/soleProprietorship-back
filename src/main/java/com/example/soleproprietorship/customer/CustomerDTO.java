package com.example.soleproprietorship.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter

public class CustomerDTO {
    @NotNull
    private Long idCustomer;
    private String name;
    private String surName;
    private String address;
    private String phoneNumber;
    private String email;

    public CustomerDTO(Long idCustomer, String name, String surName,
                       String address, String phoneNumber, String email) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.surName = surName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


}
