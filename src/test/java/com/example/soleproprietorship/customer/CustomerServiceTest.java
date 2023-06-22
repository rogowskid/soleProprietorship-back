package com.example.soleproprietorship.customer;

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
public class CustomerServiceTest {
    @Mock
    private CustomerRepository repository;
    @Mock
    private MyUserDetailsService userDetailsService;
    @InjectMocks
    private CustomerService customerService;

    @Test
    public void testGetCustomer() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Customer customerFromService = new Customer("name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        CustomerDTO customerDTOToTest = new CustomerDTO(10L, "name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        when(repository.findByIdCustomerAndUser(10L, userToTest)).thenReturn(customerFromService);
        CustomerDTO customerDTOFromService = customerService.getCustomer(10L);
        assertEquals(customerDTOToTest.getName(), customerDTOFromService.getName());
    }

    @Test
    public void testGetCustomerExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customerService.getCustomer(10L));
        assertEquals(exception.getMessage(), "Klient nie istnieje!");
    }

    @Test
    public void testGetUserCustomers() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Customer customerToTest = new Customer("name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        CustomerDTO customerDTOToTest = new CustomerDTO(10L, "name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        List<Customer> customersToTest = new LinkedList<>();
        List<CustomerDTO> customersDTOToTest = new LinkedList<>();
        customersToTest.add(customerToTest);
        customersDTOToTest.add(customerDTOToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(customersToTest);
        List<CustomerDTO> customersDTOFromService = customerService.getUserCustomers();
        assertEquals(customersDTOToTest.get(0).getName(), customersDTOFromService.get(0).getName());
    }

    @Test
    public void testGetUserCustomersExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customerService.getUserCustomers());
        assertEquals(exception.getMessage(), "Użytkownik nie posiada żadnych klientów!");
    }

    @Test
    public void testAddCustomer() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        CustomerCreationDTO customerCreationDTO = new CustomerCreationDTO();
        customerCreationDTO.setName("name");
        customerCreationDTO.setSurName("surname");
        customerCreationDTO.setAddress("address");
        customerCreationDTO.setPhoneNumber("123456789");
        customerCreationDTO.setEmail("jan.kowalski@email.pl");
        customerService.addCustomer(customerCreationDTO);
    }

    @Test
    public void testEditCustomer() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Customer customerToTest = new Customer("name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        CustomerDTO customerDTOToTest = new CustomerDTO(10L, "name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        when(repository.findByIdCustomerAndUser(customerDTOToTest.getIdCustomer(), userToTest)).thenReturn(customerToTest);
        customerService.editCustomer(customerDTOToTest);
    }

    @Test
    public void testEditCustomerExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        CustomerDTO customerDTOToTest = new CustomerDTO(10L, "name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customerService.editCustomer(customerDTOToTest));
        assertEquals(exception.getMessage(), "Klient nie istnieje!");
    }

    @Test
    public void testDeleteCustomer() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        Customer customerToTest = new Customer("name", "surname", "address", "123456789", "jan.kowalski@email.pl");
        when(repository.findByIdCustomerAndUser(10L, userToTest)).thenReturn(customerToTest);
        customerService.deleteCustomer(10L);
    }

    @Test
    public void testDeleteCustomerExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomer(10L));
        assertEquals(exception.getMessage(), "Klient nie istnieje!");
    }
}
