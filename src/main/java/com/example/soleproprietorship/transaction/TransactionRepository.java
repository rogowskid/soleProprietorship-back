package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    Transaction findByIdTransactionAndUser(Long idTransaction, User user);
}
