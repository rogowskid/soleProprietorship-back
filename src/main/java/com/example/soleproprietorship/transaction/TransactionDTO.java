package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.job.Job;
import com.example.soleproprietorship.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/***
 * DTO Transakcji.
 */
@Getter
@Setter
public class TransactionDTO {
    @NotNull
    private Long idTransaction;
    private LocalDateTime date;
    private Double price;
    private String description;
    private String customerName;
    private Integer numberOfProducts;
    private Integer numberOfJobs;
    private List<Product> products;
    private List<Job> jobs;

    public TransactionDTO(Long idTransaction, LocalDateTime date, Double price, String description,
                          String customerName, Integer numberOfProducts, Integer numberOfJobs) {
        this.idTransaction = idTransaction;
        this.date = date;
        this.price = price;
        this.description = description;
        this.customerName = customerName;
        this.numberOfProducts = numberOfProducts;
        this.numberOfJobs = numberOfJobs;
    }

    public TransactionDTO(Long idTransaction, LocalDateTime date, Double price, String description,
                          String customerName, Integer numberOfProducts, Integer numberOfJobs,
                          List<Product> products, List<Job> jobs) {
        this.idTransaction = idTransaction;
        this.date = date;
        this.price = price;
        this.description = description;
        this.customerName = customerName;
        this.numberOfProducts = numberOfProducts;
        this.numberOfJobs = numberOfJobs;
        this.products = products;
        this.jobs = jobs;
    }
}
