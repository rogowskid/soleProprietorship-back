package com.example.soleproprietorship.product;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private MyUserDetailsService userDetailsService;

    public ProductDTO getProduct(Long idProduct) {
        User user = userDetailsService.getUserFromToken();
        Product product = repository.findByIdProductAndUser(idProduct, user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        return mapEntityToDTO(product);
    }

    public List<ProductDTO> getUserProducts() {
        User user = userDetailsService.getUserFromToken();
        List<Product> products = repository.findAllByUser(user);
        if (products == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych produktów!");
        }
        return products.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void addProduct(ProductCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Product product = mapCreationDTOToEntity(dto);
        product.setUser(user);
        repository.save(product);
    }

    public void editProduct(ProductDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Product product = repository.findByIdProductAndUser(dto.getIdProduct(), user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        product.setName(dto.getName() != null ? dto.getName() : product.getName());
        product.setPrice(dto.getPrice() != null ? dto.getPrice() : product.getPrice());
        product.setWeight(dto.getWeight() != null ? dto.getWeight() : product.getWeight());
        repository.save(product);
    }

    public void deleteProduct(long idProduct) {
        User user = userDetailsService.getUserFromToken();
        Product product = repository.findByIdProductAndUser(idProduct, user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        repository.delete(product);
    }

    private ProductDTO mapEntityToDTO(Product product) {
        return new ProductDTO(product.getIdProduct(), product.getName(), product.getPrice(), product.getWeight());
    }

    private Product mapCreationDTOToEntity(ProductCreationDTO dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getWeight());
    }
}