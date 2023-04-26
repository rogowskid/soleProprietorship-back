package com.example.soleproprietorship.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class JobService {

    @Autowired
    private JobRepository repository;

    public Job getJob(Long idJob) {

        return getIsExistById(idJob);
    }

    public List<Job> getJobs(){

        return repository.findAll();
    }

    public void deleteJob(Long idJob) {
        Job job = getIsExistById(idJob);

        repository.delete(job);
    }



    private Job getIsExistById(Long idJob) {

        Job job = repository.findById(idJob).orElse(null);
        if (Objects.isNull(job))
            throw new NoSuchElementException("Usługa nie istnieje!");

        return job;

    }


}
