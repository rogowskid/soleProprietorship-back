package com.example.soleproprietorship.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ProductDTO> getCustomer(@RequestParam Long idProduct) {
        return new ResponseEntity<>(productService.getProduct(idProduct), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ProductDTO>> getUserProducts() {
        return new ResponseEntity<>(productService.getUserProducts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductCreationDTO dto) {
        productService.addProduct(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> editProduct(@Valid @RequestBody ProductDTO dto) {
        productService.editProduct(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam Long idProduct) {
        productService.deleteProduct(idProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
