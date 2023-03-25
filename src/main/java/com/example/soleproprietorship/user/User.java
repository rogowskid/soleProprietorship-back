package com.example.soleproprietorship.user;

import com.example.soleproprietorship.customer.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Entity(name = "Users")
@Getter
@Setter
public class User {
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

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "secondName")
    private String secondName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole")
    private Role role;

    public User() {
    }

    public User(String userName, String password, String email, String pesel, String firstName, String secondName, Role role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.pesel = pesel;
        this.firstName = firstName;
        this.secondName = secondName;
        this.role = role;
    }

    public User(String userName, String password, String email, String pesel, String firstName, String secondName) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.pesel = pesel;
        this.firstName = firstName;
        this.secondName = secondName;
    }
}
