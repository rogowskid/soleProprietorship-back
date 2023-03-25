package com.example.soleproprietorship.config.authentication.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String role;

    @NotBlank
    @Size(max = 30)
    private String userFirstName;

    @NotBlank
    @Size(max = 40)
    private String userSecondName;
    @NotNull
    @Size(max = 40)
    private String password;

    @NotNull
    @Size(min = 11, max = 11)
    private String pesel;
}
