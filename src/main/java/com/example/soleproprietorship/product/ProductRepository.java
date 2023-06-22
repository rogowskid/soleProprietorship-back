package com.example.soleproprietorship.product;

import com.example.soleproprietorship.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * Repozytorium produktu.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /***
     * Metoda sluzaca do znalezienia wszystkich produktow danego uzytkownika.
     * @param user Uzytkownik
     * @return Lista produktow uzytkownika
     */
    List<Product> findAllByUser(User user);

    /***
     * Metoda sluzaca do znalezienia produktu na podstawie ID Produktu i obiektu uzytkownika.
     * @param idProduct ID Produktu
     * @param user Uzytkownik
     * @return Produkt
     */
    Product findByIdProductAndUser(Long idProduct, User user);

    /***
     * Metoda sluzaca do znalezienia listy produktow na podstawie listy ID produktow.
     * @param idOfProducts Lista ID Produktow
     * @return Lista produktow
     */
    List<Product> findAllByIdProductIn(List<Long> idOfProducts);
}
