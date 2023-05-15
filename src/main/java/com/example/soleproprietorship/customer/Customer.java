package com.example.soleproprietorship.customer;

import com.example.soleproprietorship.common.HasModel;
import com.example.soleproprietorship.transaction.Transaction;
import com.example.soleproprietorship.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "Customers")
@Getter
@Setter
public class Customer implements HasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCustomer")
    private long idCustomer;

    @Column(name = "name")
    private String name;

    @Column(name = "surName")
    private String surName;

    @Column(name = "address")
    private String address;

    @Column(name = "phoneNumber", length = 9)
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Transaction> transactions;

    public Customer() {
    }

    public Customer(String name, String surName, String address, String phoneNumber, String email) {
        this.name = name;
        this.surName = surName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
