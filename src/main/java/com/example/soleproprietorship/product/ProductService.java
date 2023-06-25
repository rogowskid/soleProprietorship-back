package com.example.soleproprietorship.product;

import com.example.soleproprietorship.common.EntityDTO;
import com.example.soleproprietorship.common.EntityModelValid;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.user.User;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/***
 * Serwis dotyczacy produktu.
 */
@Service
public class ProductService extends EntityDTO<Product, ProductCreationDTO, ProductDTO>
        implements EntityModelValid<Product, Long> {
    private ProductRepository repository;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public ProductService(ProductRepository repository, MyUserDetailsService userDetailsService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
    }

    /***
     * Metoda zwracajaca DTO produktu na podstawie ID produktu.
     * @param idProduct ID Produktu
     * @return DTO Produktu
     */
    public ProductDTO getProduct(Long idProduct) {
        User user = userDetailsService.getUserFromToken();
        Product product = repository.findByIdProductAndUser(idProduct, user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        return mapEntityToDTO(product);
    }

    /***
     * Metoda zwracajaca liste DTO produktow uzytkownika.
     * @return Lista DTO produktow uzytkownika
     */
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

    /***
     * Metoda sluzaca dodawaniu nowych produktow do systemu.
     * @param dto DTO Produktu
     * @return
     */
    public void addProduct(ProductCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Product product = mapCreationDTOToEntity(dto);
        product.setUser(user);
        repository.save(product);
    }

    /***
     * Metoda sluzaca edycji produktu.
     * @param dto DTO Produktu
     * @return
     */
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

    /***
     * Metoda sluzaca usunieciu produktu.
     * @param idProduct ID Produktu
     * @param verifyCode
     * @return
     */
    public void deleteProduct(long idProduct, String verifyCode) {
        User user = userDetailsService.getUserFromToken();

        validate2FA(user, verifyCode);

        Product product = repository.findByIdProductAndUser(idProduct, user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        repository.delete(product);
    }

    /***
     * Mapper encji na DTO.
     * @param product Encja produktu
     * @return DTO produktu
     */
    @Override
    protected ProductDTO mapEntityToDTO(Product product) {
        return new ProductDTO(product.getIdProduct(), product.getName(), product.getPrice(), product.getWeight());
    }

    public List<ProductDTO> mapEntitiesToDTO(List<Product> products) {
        if(products == null || products.isEmpty()) {
            return null;
        }
        return products.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
    }

    /***
     * Mapper DTO na encje.
     * @param dto DTO produktu
     * @return Encja produktu
     */
    @Override
    protected Product mapCreationDTOToEntity(ProductCreationDTO dto) {
        return new Product(dto.getName(), dto.getPrice(), dto.getWeight());
    }

    @Override
    public Product executeEncode(Product entity) {
        Product product = new Product();

        product.setIdProduct(entity.getIdProduct());
        product.setWeight(entity.getWeight());
        product.setPrice(entity.getPrice());
        product.setTransactions(entity.getTransactions());
        product.setName(Encode.forHtml(entity.getName()));
        product.setUser(entity.getUser());

        return product;
    }

    @Override
    public List<Product> executeEncodeList(List<Product> entities) {

        ArrayList<Product> products = new ArrayList<>();

        for (Product entity : entities) {
            products.add(executeEncode(entity));
        }

        return products;
    }

    @Override
    public Product getEntity(Long idProduct) {
        User user = userDetailsService.getUserFromToken();
        Product product = repository.findByIdProductAndUser(idProduct, user);
        if (product == null) {
            throw new NoSuchElementException("Produkt nie istnieje!");
        }
        return product;
    }

    @Override
    public List<Product> getEntities() {
        User user = userDetailsService.getUserFromToken();
        List<Product> products = repository.findAllByUser(user);
        if (products == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych produktów!");
        }
        return products;
    }
}
