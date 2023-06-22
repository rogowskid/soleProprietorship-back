package com.example.soleproprietorship.job;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/***
 * DTO tworzenia uslugi.
 */
@Getter
@Setter
public class JobCreationDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String price;
}
