package com.example.soleproprietorship.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String phoneNumber;
    private String pesel;
    private String firstName;
    private String surName;
    private String address;
    private boolean isUsing2FA;
    public UserDTO(String email, String phoneNumber, String pesel, String firstName, String surName, String address, boolean isUsing2FA) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pesel = pesel;
        this.firstName = firstName;
        this.surName = surName;
        this.address = address;
        this.isUsing2FA = isUsing2FA;
    }
}
