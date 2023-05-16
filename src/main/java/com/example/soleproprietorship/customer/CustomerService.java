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

@Service
public class CustomerService extends EntityDTO<Customer, CustomerCreationDTO, CustomerDTO> implements EntityModelValid<Customer, Long> {

    @Autowired
    private CustomerRepository repository;
    @Autowired
    private MyUserDetailsService userDetailsService;

    public CustomerDTO getCustomer(Long idCustomer) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(idCustomer, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        return mapEntityToDTO(customer);
    }

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

    public void addCustomer(CustomerCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = mapCreationDTOToEntity(dto);
        customer.setUser(user);
        repository.save(customer);
    }

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

    public void deleteCustomer(long idCustomer) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(idCustomer, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        repository.delete(customer);
    }

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

    @Override
    public Customer getEntity(Long id) {
        User user = userDetailsService.getUserFromToken();
        Customer customer = repository.findByIdCustomerAndUser(id, user);
        if (customer == null) {
            throw new NoSuchElementException("Klient nie istnieje!");
        }
        return customer;
    }

    @Override
    public List<Customer> getEntities() {
        return repository.findAll();
    }

    @Override
    protected CustomerDTO mapEntityToDTO(Customer customer) {
        return new CustomerDTO(customer.getIdCustomer(), customer.getName(), customer.getSurName(),
                customer.getAddress(), customer.getPhoneNumber(), customer.getEmail());
    }

    @Override
    protected Customer mapCreationDTOToEntity(CustomerCreationDTO dto) {
        return new Customer(dto.getName(), dto.getSurName(), dto.getAddress(), dto.getPhoneNumber(), dto.getEmail());

    }
}
