package com.example.soleproprietorship.job;

import com.example.soleproprietorship.common.HasModel;
import com.example.soleproprietorship.transaction.Transaction;
import com.example.soleproprietorship.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Jobs")
@Getter
@Setter
public class Job implements HasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idJob")
    private long idJob;

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
    }, mappedBy = "jobs")
    private List<Transaction> transactions;

    public Job() {
    }

    public Job(String name, String price) {
        this.name = name;
        this.price = price;
    }
}
