package com.example.soleproprietorship.job;

import com.example.soleproprietorship.common.EntityModelValid;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class JobService implements EntityModelValid<Job, Long> {

    @Autowired
    private JobRepository repository;

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


    @Override
    public Job executeEncode(Job entity) {

        Job job = new Job();

        job.setName(Encode.forHtml(entity.getName()));
        job.setUser(entity.getUser());
        job.setTransactions(entity.getTransactions());
        job.setIdJob(entity.getIdJob());
        job.setPrice(entity.getPrice());
        return job;
    }

    @Override
    public List<Job> executeEncodeList(List<Job> entities) {
        ArrayList<Job> jobs = new ArrayList<>();
        for (Job entity : entities) {
            jobs.add(executeEncode(entity));
        }

        return jobs;
    }

    @Override
    public Job getEntity(Long idJob) {
        return getIsExistById(idJob);

    }

    @Override
    public List<Job> getEntities() {
        return repository.findAll();

    }

}
