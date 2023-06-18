package com.example.soleproprietorship.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class TransactionCreationDTO {

    @NotNull
    private Double price;
    @NotBlank
    private String description;
    @NotNull
    private Long idCustomer;
    @NotNull
    private List<Long> idOfProducts;
    @NotNull
    private List<Long> idOfJobs;
}
