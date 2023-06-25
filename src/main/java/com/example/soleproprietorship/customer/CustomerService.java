package com.example.soleproprietorship.customer;

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
 * Serwis dotyczacy klienta.
 */
@Service
public class CustomerService extends EntityDTO<Customer, CustomerCreationDTO, CustomerDTO> implements EntityModelValid<Customer, Long> {
    private CustomerRepository repository;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public CustomerService(CustomerRepository repository, MyUserDetailsService userDetailsService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
    }

    /***
     * Metoda zwracajaca DTO klienta dla podanego ID Klienta.
     * @param idCustomer ID Klienta
     * @return DTO Klienta
     */
    public CustomerDTO getCustomer(Long idCustomer) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(idCustomer, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        return mapEntityToDTO(customer);
    }

    /***
     * Metoda zwracajaca liste DTO klientow posiadanych przez danego uzytkownika.
     * @return Lista DTO klientow
     */
    public List<CustomerDTO> getUserCustomers() {
        User user = userDetailsService.getUserFromToken();
        List<Customer> customers = repository.findAllByUser(user);
        if (customers == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych klientów!");
        }
        return customers.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    /***
     * Metoda sluzaca dodaniu klienta do systemu.
     * @param dto DTO klienta
     * @return
     */
    public void addCustomer(CustomerCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = mapCreationDTOToEntity(dto);
        customer.setUser(user);
        repository.save(customer);
    }

    /***
     * Metoda sluzaca edycji klienta w systemie.
     * @param dto DTO klienta
     * @return
     */
    public void editCustomer(CustomerDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(dto.getIdCustomer(), user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        customer.setName(dto.getName() != null ? dto.getName() : customer.getName());
        customer.setSurName(dto.getSurName() != null ? dto.getSurName() : customer.getSurName());
        customer.setAddress(dto.getAddress() != null ? dto.getAddress() : customer.getAddress());
        customer.setEmail(dto.getEmail() != null ? dto.getEmail() : customer.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : customer.getPhoneNumber());
        repository.save(customer);
    }

    /***
     * Metoda sluzaca usunieciu klienta z systemu.
     * @param idCustomer ID klienta
     * @param verifyCode
     * @return
     */
    public void deleteCustomer(long idCustomer, String verifyCode) {
        User user = userDetailsService.getUserFromToken();
        validate2FA(user, verifyCode);

        Customer customer = repository.findByIdCustomerAndUser(idCustomer, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        repository.delete(customer);
    }

    /**
     * Metoda służąca do "Escapowania" nielegalnych znaków
     * @param model
     * @return
     */
    @Override
    public Customer executeEncode(Customer model) {
        Customer customer = new Customer();
        customer.setIdCustomer(customer.getIdCustomer());
        customer.setName(Encode.forHtml(model.getName()));
        customer.setSurName(Encode.forHtml(model.getSurName()));
        customer.setEmail(parseEmail(model.getEmail()));
        customer.setPhoneNumber(parsePhoneNumber(model.getPhoneNumber()));
        customer.setTransactions(model.getTransactions());
        customer.setUser(model.getUser());
        return customer;
    }

    @Override
    public List<Customer> executeEncodeList(List<Customer> models) {
        ArrayList<Customer> customers = new ArrayList<>();
        for (Customer model : models) {
            customers.add(executeEncode(model));
        }
        return customers;

    }

    /**
     * Za pomocą wybranego id zwraca wybranego Customera
     * @param id
     * @return
     */
    @Override
    public Customer getEntity(Long id) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(id, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        return customer;
    }

    /**
     * Zwraca wszystkie obiekty customera
     * @return
     */
    @Override
    public List<Customer> getEntities() {
        return repository.findAll();
    }

    /***
     * Mapper encji na DTO.
     * @param customer Encja klienta
     * @return DTO klienta
     */
    @Override
    protected CustomerDTO mapEntityToDTO(Customer customer) {
        return new CustomerDTO(customer.getIdCustomer(), customer.getName(), customer.getSurName(),
                customer.getAddress(), customer.getPhoneNumber(), customer.getEmail());
    }

    /***
     * Mapper DTO tworzenia klienta na encje.
     * @param dto DTO tworzenia klienta.
     * @return Encja klienta
     */
    @Override
    protected Customer mapCreationDTOToEntity(CustomerCreationDTO dto) {
        return new Customer(dto.getName(), dto.getSurName(), dto.getAddress(), dto.getPhoneNumber(), dto.getEmail());

    }
}
