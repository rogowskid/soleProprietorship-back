package com.example.soleproprietorship.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<JobDTO> getJob(@RequestParam Long idJob) {
        return new ResponseEntity<>(jobService.getJob(idJob), HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getUserJobs() {
        return new ResponseEntity<>(jobService.getUserJobs(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addJob(@Valid @RequestBody JobCreationDTO dto) {
        jobService.addJob(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> editJob(@Valid @RequestBody JobDTO dto) {
        jobService.editJob(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteJob(@RequestParam Long idJob) {
        jobService.deleteJob(idJob);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
