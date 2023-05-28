package com.example.soleproprietorship.customer.role;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRole")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "name")
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }

    public Role() {
    }
}
