package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * Repozytorium transakcji.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /***
     * Metoda umozliwiajaca znalezienie listy transakcji na podstawie obiektu uzytkownika.
     * @param user Obiekt uzytkownika
     * @return Lista transakcji
     */
    List<Transaction> findAllByUser(User user);

    /***
     * Metoda umozliwiajaca znalezienie transakcji na podstawie ID transakcji i obiektu uzytkownika.
     * @param idTransaction ID transakcji
     * @param user Uzytkownik
     * @return Transakcja
     */
    Transaction findByIdTransactionAndUser(Long idTransaction, User user);
}
