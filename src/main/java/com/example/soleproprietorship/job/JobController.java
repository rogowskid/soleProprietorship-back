package com.example.soleproprietorship.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService service;


    @GetMapping()
    public ResponseEntity<Job> getJob(@RequestParam Long idJob) {

        return new ResponseEntity<>(service.getJob(idJob), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getJobs() {

        return new ResponseEntity<>(service.getJobs(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteJob(@RequestParam Long idJob) {
        service.deleteJob(idJob);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
