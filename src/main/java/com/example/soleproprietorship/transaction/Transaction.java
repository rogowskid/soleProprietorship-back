package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.customer.Customer;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.service.Service;
import com.example.soleproprietorship.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransaction")
    private long idTransaction;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "TransactionProducts",
            joinColumns = {
                @JoinColumn(name = "idTransaction")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "idProduct")
            }
    )
    private List<Product> products;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "TransactionServices",
            joinColumns = {
                    @JoinColumn(name = "idTransaction")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "idService")
            }
    )
    private List<Service> services;

    public Transaction() {
    }
}
