package com.example.soleproprietorship.customer;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByUser(User user);
    Customer findByIdCustomerAndUser(Long idCustomer, User user);
}
