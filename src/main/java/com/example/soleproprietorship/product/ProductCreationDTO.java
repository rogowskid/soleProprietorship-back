package com.example.soleproprietorship.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductCreationDTO {
    @NotBlank
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private Double weight;
}
