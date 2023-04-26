package com.example.soleproprietorship.service;

import com.example.soleproprietorship.transaction.Transaction;
import com.example.soleproprietorship.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Services")
@Getter
@Setter
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idService")
    private long idService;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, mappedBy = "services")
    private List<Transaction> transactions;

    public Service() {
    }
}
