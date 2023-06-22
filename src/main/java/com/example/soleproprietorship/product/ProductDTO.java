package com.example.soleproprietorship.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/***
 * DTO Produktu.
 */
@Getter
@Setter
public class ProductDTO {
    @NotNull
    private Long idProduct;
    private String name;
    private Double price;
    private Double weight;

    public ProductDTO(Long idProduct, String name, Double price, Double weight) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }
}
