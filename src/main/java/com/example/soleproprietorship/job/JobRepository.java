package com.example.soleproprietorship.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository <Job, Long> {
    List<Job> findAllByIdJobIn(List<Long> idOfJobs);
}
