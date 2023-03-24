package com.example.soleproprietorship.customer.role;

import jakarta.persistence.*;

@Entity(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "name")
    private ERole name;
}
