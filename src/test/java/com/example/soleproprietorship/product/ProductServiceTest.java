package com.example.soleproprietorship.product;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository repository;
    @Mock
    private MyUserDetailsService userDetailsService;
    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetProduct() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Product productFromService = new Product("name", 10.0, 15.0);
        ProductDTO productDTOToTest = new ProductDTO(10L, "name", 10.0, 15.0);
        when(repository.findByIdProductAndUser(10L, userToTest)).thenReturn(productFromService);
        ProductDTO productDTOFromService = productService.getProduct(10L);
        assertEquals(productDTOToTest.getName(), productDTOFromService.getName());
    }

    @Test
    public void testGetProductExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> productService.getProduct(10L));
        assertEquals(exception.getMessage(), "Produkt nie istnieje!");
    }

    @Test
    public void testGetUserProducts() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Product productToTest = new Product("name", 10.0, 15.0);
        ProductDTO productDTOToTest = new ProductDTO(10L, "name", 10.0, 15.0);
        List<Product> productsToTest = new LinkedList<>();
        List<ProductDTO> productsDTOToTest = new LinkedList<>();
        productsToTest.add(productToTest);
        productsDTOToTest.add(productDTOToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(productsToTest);
        List<ProductDTO> productsDTOFromService = productService.getUserProducts();
        assertEquals(productsDTOToTest.get(0).getName(), productsDTOFromService.get(0).getName());
    }

    @Test
    public void testGetUserProductsExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> productService.getUserProducts());
        assertEquals(exception.getMessage(), "Użytkownik nie posiada żadnych produktów!");
    }

    @Test
    public void testAddProduct() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        ProductCreationDTO productCreationDTO = new ProductCreationDTO();
        productCreationDTO.setName("name");
        productCreationDTO.setPrice(10.0);
        productCreationDTO.setWeight(15.0);
        productService.addProduct(productCreationDTO);
    }

    @Test
    public void testEditProduct() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Product productToTest = new Product("name", 10.0, 15.0);
        ProductDTO productDTOToTest = new ProductDTO(10L, "name", 10.0, 15.0);
        when(repository.findByIdProductAndUser(productDTOToTest.getIdProduct(), userToTest)).thenReturn(productToTest);
        productService.editProduct(productDTOToTest);
    }

    @Test
    public void testEditProductExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        ProductDTO productDTOToTest = new ProductDTO(10L, "name", 10.0, 15.0);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> productService.editProduct(productDTOToTest));
        assertEquals(exception.getMessage(), "Produkt nie istnieje!");
    }

    @Test
    public void testDeleteProduct() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Product productToTest = new Product("name", 10.0, 15.0);
        when(repository.findByIdProductAndUser(10L, userToTest)).thenReturn(productToTest);
        productService.deleteProduct(10L, null);
    }

    @Test
    public void testDeleteProductExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> productService.deleteProduct(10L, null));
        assertEquals(exception.getMessage(), "Produkt nie istnieje!");
    }
}
