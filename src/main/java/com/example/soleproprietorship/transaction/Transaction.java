package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.common.HasModel;
import com.example.soleproprietorship.customer.Customer;
import com.example.soleproprietorship.job.Job;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
public class Transaction implements HasModel {
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "TransactionServices",
            joinColumns = {
                    @JoinColumn(name = "idTransaction")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "idJob")
            }
    )
    private List<Job> jobs;

    public Transaction() {
    }

    public Transaction(LocalDateTime date, Double price, String description) {
        this.date = date;
        this.price = price;
        this.description = description;
    }
}
