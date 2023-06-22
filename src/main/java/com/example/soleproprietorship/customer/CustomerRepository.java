package com.example.soleproprietorship.customer;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * Repozytorium klienta.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /***
     * Metoda sluzaca do znajdowania klientow uzytkownika w bazie danych.
     * @param user Uzytkownik
     * @return Lista klientow uzytkownika
     */
    List<Customer> findAllByUser(User user);

    /***
     * Metoda sluzaca do znajdowania klienta na podstawie ID klienta i obiektu uzytkownika.
     * @param idCustomer ID klienta
     * @param user Uzytkownik
     * @return Klient
     */
    Customer findByIdCustomerAndUser(Long idCustomer, User user);
}
