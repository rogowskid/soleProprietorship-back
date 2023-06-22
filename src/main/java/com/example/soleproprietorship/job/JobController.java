package com.example.soleproprietorship.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 * Kontroler dotyczacy uslugi.
 */
@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    /***
     * Metoda zwracajaca DTO uslugi na podstawie ID uslugi.
     * @param idJob ID uslugi
     * @return DTO uslugi
     */
    @GetMapping
    public ResponseEntity<JobDTO> getJob(@RequestParam Long idJob) {
        return new ResponseEntity<>(jobService.getJob(idJob), HttpStatus.OK);
    }

    /***
     * Metoda zwracajaca liste DTO uslug uzytkownika.
     * @return Lista DTO uslug uzytkownika.
     */
    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getUserJobs() {
        return new ResponseEntity<>(jobService.getUserJobs(), HttpStatus.OK);
    }

    /***
     * Metoda sluzaca do dodania nowej uslugi do systemu.
     * @param dto DTO uslugi
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addJob(@Valid @RequestBody JobCreationDTO dto) {
        jobService.addJob(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca do edycji uslugi w systemie.
     * @param dto DTO uslugi
     * @return
     */
    @PatchMapping
    public ResponseEntity<Void> editJob(@Valid @RequestBody JobDTO dto) {
        jobService.editJob(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca do usuniecia uslugi z systemu.
     * @param idJob ID Uslugi
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteJob(@RequestParam Long idJob) {
        jobService.deleteJob(idJob);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
