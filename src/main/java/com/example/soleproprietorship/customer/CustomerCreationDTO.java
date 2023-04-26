package com.example.soleproprietorship.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CustomerCreationDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String surName;
    @NotBlank
    private String address;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String email;
}
