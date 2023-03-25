package com.example.soleproprietorship.customer.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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


}
