package com.example.soleproprietorship.customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Customer")
@Getter
@Setter
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCustomer;
    @Column(name = "name")
    private String name;

    @Column(name="surName")
    private String surName;

    @Column(name="address")
    private String address;

    @Column(name="phoneNumber")
    @Size(min = 9, max = 9)
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;


}
