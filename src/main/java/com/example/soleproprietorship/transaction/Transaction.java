package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "Transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long idTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @Column(name = "dateOfTransaction")
    private LocalDateTime dateOfTransaction;

    @Column(name = "transactionAmount")
    private Double transactionAmount;

    @Column(name = "description")
    private String description;

    public Transaction() {

    }

    public Transaction(Customer customer, Double transactionAmount, String description) {
        this.customer = customer;
        this.dateOfTransaction = LocalDateTime.now();
        this.transactionAmount = transactionAmount;
        this.description = description;
    }


}
