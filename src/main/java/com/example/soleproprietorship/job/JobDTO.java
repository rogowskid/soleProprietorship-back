package com.example.soleproprietorship.job;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/***
 * DTO uslugi.
 */
@Getter
@Setter
public class JobDTO {
    @NotNull
    private Long idJob;
    private String name;
    private String price;

    public JobDTO(Long idJob, String name, String price) {
        this.idJob = idJob;
        this.name = name;
        this.price = price;
    }
}
