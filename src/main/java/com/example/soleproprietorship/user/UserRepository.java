package com.example.soleproprietorship.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/***
 * Repozytorium uzytkownika.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /***
     * Metoda zwracajaca uzytkownika na podstawie nazwy uzytkownika.
     * @param userName Nazwa uzytkownika.
     * @return Uzytkownik
     */
    Optional<User> findByUserName(String userName);

    /***
     * Metoda sprawdzajaca czy uzytkownik istnieje w systemie na podstawie nazwy uzytkownika.
     * @param userName Nazwa uzytkownika.
     * @return Czy uzytkownik istnieje w systemie.
     */
    Boolean existsByUserName(String userName);

    /***
     * Metoda sprawdzajaca czy uzytkownik istnieje w systemie na podstawie email.
     * @param email Adres email.
     * @return Czy uzytkownik istnieje w systemie.
     */
    Boolean existsByEmail(String email);

}
