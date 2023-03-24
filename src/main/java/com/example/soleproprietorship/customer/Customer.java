package com.example.soleproprietorship.customer;

import com.example.soleproprietorship.customer.role.Role;
import com.example.soleproprietorship.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "Customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long idCustomer;
    @Column(name = "name")
    private String name;

    @Column(name = "surName")
    private String surName;

    @Column(name = "address")
    private String address;

    @Column(name = "phoneNumber", length = 10)
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "Customers")
    private List<Transaction> transactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Role role;

    public Customer() {

    }


}
