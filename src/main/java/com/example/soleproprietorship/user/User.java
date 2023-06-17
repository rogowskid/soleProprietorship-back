package com.example.soleproprietorship.user;

import com.example.soleproprietorship.common.HasModel;
import com.example.soleproprietorship.customer.Customer;
import com.example.soleproprietorship.customer.role.Role;
import com.example.soleproprietorship.job.Job;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User implements HasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransaction")
    private Long idUser;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phoneNumber", length = 9)
    private String phoneNumber;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "surName")
    private String surName;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole")
    private Role role;

    //OAuth2 fields
    private String provider;
    private String providerId;

    //2FA fields
    @Column(name = "isUsing2FA")
    private boolean isUsing2FA;

    @Column(name = "secret2FA")
    private String secret2FA;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Product> products;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Job> jobs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Customer> customers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Transaction> transactions;

    public User() {
    }

    public User(String userName, String password, String email, String pesel, String firstName, String surName) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.pesel = pesel;
        this.firstName = firstName;
        this.surName = surName;
    }

    public User(String userName, String password, String email, String phoneNumber, String pesel, String firstName,
                String surName, String address, Role role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pesel = pesel;
        this.firstName = firstName;
        this.surName = surName;
        this.address = address;
        this.role = role;
    }
}
