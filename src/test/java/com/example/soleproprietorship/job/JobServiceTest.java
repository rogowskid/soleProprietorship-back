package com.example.soleproprietorship.job;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.product.ProductCreationDTO;
import com.example.soleproprietorship.product.ProductDTO;
import com.example.soleproprietorship.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JobServiceTest {
    @Mock
    private JobRepository repository;
    @Mock
    private MyUserDetailsService userDetailsService;
    @InjectMocks
    private JobService jobService;

    @Test
    public void testGetJob() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Job jobFromService = new Job("name", "10.0");
        JobDTO jobDTOToTest = new JobDTO(10L, "name", "10.0");
        when(repository.findByIdJobAndUser(10L, userToTest)).thenReturn(jobFromService);
        JobDTO jobDTOFromService = jobService.getJob(10L);
        assertEquals(jobDTOToTest.getName(), jobDTOFromService.getName());
    }

    @Test
    public void testGetJobExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> jobService.getJob(10L));
        assertEquals(exception.getMessage(), "Usługa nie istnieją!");
    }

    @Test
    public void testGetUserJobs() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Job jobToTest = new Job("name", "10.0");
        JobDTO jobDTOToTest = new JobDTO(10L, "name", "10.0");
        List<Job> jobsToTest = new LinkedList<>();
        List<JobDTO> jobsDTOToTest = new LinkedList<>();
        jobsToTest.add(jobToTest);
        jobsDTOToTest.add(jobDTOToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(jobsToTest);
        List<JobDTO> jobsDTOFromService = jobService.getUserJobs();
        assertEquals(jobsDTOToTest.get(0).getName(), jobsDTOFromService.get(0).getName());
    }

    @Test
    public void testGetUserJobsExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> jobService.getUserJobs());
        assertEquals(exception.getMessage(), "Użytkownik nie posiada żadnych usług!");
    }

    @Test
    public void testAddJob() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        JobCreationDTO jobCreationDTO = new JobCreationDTO();
        jobCreationDTO.setName("name");
        jobCreationDTO.setPrice("10.0");
        jobService.addJob(jobCreationDTO);
    }

    @Test
    public void testEditJob() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Job jobToTest = new Job("name", "10.0");
        JobDTO jobDTOToTest = new JobDTO(10L, "name", "10.0");
        when(repository.findByIdJobAndUser(jobDTOToTest.getIdJob(), userToTest)).thenReturn(jobToTest);
        jobService.editJob(jobDTOToTest);
    }

    @Test
    public void testEditJobExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        JobDTO jobDTOToTest = new JobDTO(10L, "name", "10.0");
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> jobService.editJob(jobDTOToTest));
        assertEquals(exception.getMessage(), "Usługa nie istnieje!");
    }

    @Test
    public void testDeleteJob() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Job jobToTest = new Job("name", "10.0");
        when(repository.findByIdJobAndUser(10L, userToTest)).thenReturn(jobToTest);
        jobService.deleteJob(10L);
    }

    @Test
    public void testDeleteJobExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> jobService.deleteJob(10L));
        assertEquals(exception.getMessage(), "Usługa nie istnieje!");
    }
}
