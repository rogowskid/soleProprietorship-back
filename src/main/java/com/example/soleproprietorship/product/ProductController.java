package com.example.soleproprietorship.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 * Kontroler dotyczacy produktu.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /***
     * Metoda zwracajaca DTO produktu na podstawie ID produktu.
     * @param idProduct ID Produktu
     * @return DTO Produktu
     */
    @GetMapping
    public ResponseEntity<ProductDTO> getCustomer(@RequestParam Long idProduct) {
        return new ResponseEntity<>(productService.getProduct(idProduct), HttpStatus.OK);
    }

    /***
     * Metoda zwracajaca liste DTO produktow uzytkownika.
     * @return Lista DTO produktow uzytkownika
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getUserProducts() {
        return new ResponseEntity<>(productService.getUserProducts(), HttpStatus.OK);
    }

    /***
     * Metoda sluzaca dodawaniu nowych produktow do systemu.
     * @param dto DTO Produktu
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductCreationDTO dto) {
        productService.addProduct(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca edycji produktu.
     * @param dto DTO Produktu
     * @return
     */
    @PatchMapping
    public ResponseEntity<Void> editProduct(@Valid @RequestBody ProductDTO dto) {
        productService.editProduct(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca usunieciu produktu.
     * @param idProduct ID Produktu
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam Long idProduct, @RequestParam String verifyCode) {
        productService.deleteProduct(idProduct, verifyCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
