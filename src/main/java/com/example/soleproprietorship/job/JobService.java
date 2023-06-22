package com.example.soleproprietorship.job;

import com.example.soleproprietorship.common.EntityDTO;
import com.example.soleproprietorship.common.EntityModelValid;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.user.User;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class JobService extends EntityDTO<Job, JobCreationDTO, JobDTO> implements EntityModelValid<Job, Long> {
    private JobRepository repository;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public JobService(JobRepository repository, MyUserDetailsService userDetailsService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
    }

    /***
     * Metoda zwracajaca DTO uslugi na podstawie ID uslugi.
     * @param idJob ID uslugi
     * @return DTO uslugi
     */
    public JobDTO getJob(Long idJob) {
        User user = userDetailsService.getUserFromToken();
        Job job = repository.findByIdJobAndUser(idJob, user);
        if (job == null) {
            throw new NoSuchElementException("Usługa nie istnieją!");
        }
        return mapEntityToDTO(job);
    }

    /***
     * Metoda zwracajaca liste DTO uslug uzytkownika.
     * @return Lista DTO uslug uzytkownika.
     */
    public List<JobDTO> getUserJobs() {
        User user = userDetailsService.getUserFromToken();
        List<Job> jobs = repository.findAllByUser(user);
        if (jobs == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych usług!");
        }
        return jobs.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    /***
     * Metoda sluzaca do dodania nowej uslugi do systemu.
     * @param dto DTO uslugi
     * @return
     */
    public void addJob(JobCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Job job = mapCreationDTOToEntity(dto);
        job.setUser(user);
        repository.save(job);
    }

    /***
     * Metoda sluzaca do edycji uslugi w systemie.
     * @param dto DTO uslugi
     * @return
     */
    public void editJob(JobDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Job job = repository.findByIdJobAndUser(dto.getIdJob(), user);
        if (job == null) {
            throw new NoSuchElementException("Usługa nie istnieje!");
        }
        job.setName(dto.getName() != null ? dto.getName() : job.getName());
        job.setPrice(dto.getPrice() != null ? dto.getPrice() : job.getPrice());
        repository.save(job);
    }

    /***
     * Metoda sluzaca do usuniecia uslugi z systemu.
     * @param idJob ID Uslugi
     * @return
     */
    public void deleteJob(long idJob, String verifyCode) {
        User user = userDetailsService.getUserFromToken();
        validate2FA(user, verifyCode);
        Job job = repository.findByIdJobAndUser(idJob, user);
        if (job == null) {
            throw new NoSuchElementException("Usługa nie istnieje!");
        }
        repository.delete(job);
    }

    /***
     * Mapper encji uslugi na DTO.
     * @param job Encja uslugi
     * @return DTO uslugi
     */
    @Override
    protected JobDTO mapEntityToDTO(Job job) {
        return new JobDTO(job.getIdJob(), job.getName(), job.getPrice());
    }

    /***
     * Mapper DTO uslugi na encje.
     * @param jobCreationDTO DTO uslugi
     * @return Encja uslugi
     */
    @Override
    protected Job mapCreationDTOToEntity(JobCreationDTO jobCreationDTO) {
        return new Job(jobCreationDTO.getName(), jobCreationDTO.getPrice());
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
        User user = userDetailsService.getUserFromToken();
        Job job = repository.findByIdJobAndUser(idJob, user);
        if (job == null) {
            throw new NoSuchElementException("Usługa nie istnieje!");
        }
        return job;
    }

    @Override
    public List<Job> getEntities() {
        return repository.findAll();

    }
}
