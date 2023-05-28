package com.example.soleproprietorship.job;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository <Job, Long> {
    List<Job> findAllByIdJobIn(List<Long> idOfJobs);
    List<Job> findAllByUser(User user);
    Job findByIdJobAndUser(Long idJob, User user);
}
