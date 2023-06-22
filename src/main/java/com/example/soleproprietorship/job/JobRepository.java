package com.example.soleproprietorship.job;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * Repozytorium uslugi.
 */
@Repository
public interface JobRepository extends JpaRepository <Job, Long> {
    /***
     * Metoda sluzaca do znalezienia wszystkich uslug na podstawie ID uslug.
     * @param idOfJobs Lista ID uslug.
     * @return Lista uslug.
     */
    List<Job> findAllByIdJobIn(List<Long> idOfJobs);

    /***
     * Metoda sluzaca do znalezienia wszystkich uslug na podstawie obiektu uzytkownika.
     * @param user Uzytkownik
     * @return Lista uslug
     */
    List<Job> findAllByUser(User user);

    /***
     * Metoda sluzaca do znalezienia uslugi na podstawie ID uslugi i obiektu uzytkownika.
     * @param idJob ID uslugi
     * @param user Uzytkownik
     * @return Usluga
     */
    Job findByIdJobAndUser(Long idJob, User user);
}
