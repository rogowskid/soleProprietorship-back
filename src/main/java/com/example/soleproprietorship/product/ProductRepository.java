package com.example.soleproprietorship.product;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUser(User user);
    Product findByIdProductAndUser(Long idProduct, User user);
}
