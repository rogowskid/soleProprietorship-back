package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.customer.Customer;
import com.example.soleproprietorship.customer.CustomerRepository;
import com.example.soleproprietorship.job.Job;
import com.example.soleproprietorship.job.JobRepository;
import com.example.soleproprietorship.job.JobService;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.product.ProductRepository;
import com.example.soleproprietorship.product.ProductService;
import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private ProductService productService;

    @Mock
    private JobService jobService;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testGetTransaction() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        TransactionDTO transactionDTOToTest = new TransactionDTO(10L, dateTime, 45.50, "description",
                "Jan Kowalski", 0, 0);
        Transaction transactionToTest = new Transaction(dateTime, 45.50, "description");
        transactionToTest.setIdTransaction(10L);
        transactionToTest.setCustomer(new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl"));
        transactionToTest.setProducts(new LinkedList<>());
        transactionToTest.setJobs(new LinkedList<>());
        when(repository.findByIdTransactionAndUser(10L, userToTest)).thenReturn(transactionToTest);
        TransactionDTO transactionDTOFromService = transactionService.getTransaction(10L);
        assertEquals(transactionDTOToTest.getNumberOfJobs(), transactionDTOFromService.getNumberOfJobs());
    }

    @Test
    public void testGetTransactionExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> transactionService.getTransaction(10L));
        assertEquals(exception.getMessage(), "Transakcja nie istnieje!");
    }

    @Test
    public void testGetUserTransactions() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        Transaction transactionToTest = new Transaction(dateTime, 45.50, "description");
        transactionToTest.setIdTransaction(10L);
        transactionToTest.setCustomer(new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl"));
        transactionToTest.setProducts(new LinkedList<>());
        transactionToTest.setJobs(new LinkedList<>());
        Transaction transactionToTest2 = new Transaction(dateTime, 45.50, "description");
        transactionToTest2.setIdTransaction(10L);
        transactionToTest2.setCustomer(new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl"));
        transactionToTest2.setProducts(new LinkedList<>());
        transactionToTest2.setJobs(new LinkedList<>());
        TransactionDTO transactionDTOToTest = new TransactionDTO(10L, dateTime, 45.50, "description",
                "Jan Kowalski", 0, 0);
        TransactionDTO transactionDTOToTest2 = new TransactionDTO(10L, dateTime, 45.50, "description",
                "Jan Kowalski", 0, 0);
        List<Transaction> transactionsToTest = new LinkedList<>();
        transactionsToTest.add(transactionToTest);
        transactionsToTest.add(transactionToTest2);
        when(repository.findAllByUser(userToTest)).thenReturn(transactionsToTest);
        List<TransactionDTO> transactionDTOSFromService = transactionService.getUserTransactions();
        assertEquals(transactionDTOToTest.getNumberOfJobs(), transactionDTOSFromService.get(0).getNumberOfJobs());
        assertEquals(transactionDTOToTest2.getNumberOfJobs(), transactionDTOSFromService.get(1).getNumberOfJobs());
    }

    @Test
    public void testGetUserTransactionsExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        when(repository.findAllByUser(userToTest)).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> transactionService.getUserTransactions());
        assertEquals(exception.getMessage(), "Użytkownik nie posiada żadnych transakcji!");
    }

    @Test
    public void testAddTransaction() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Long> id = new LinkedList<>();
        id.add(5L);
        TransactionCreationDTO transactionCreationDTOToTest = new TransactionCreationDTO();
        transactionCreationDTOToTest.setPrice(45.50);
        transactionCreationDTOToTest.setDescription("description");
        transactionCreationDTOToTest.setIdCustomer(10L);
        transactionCreationDTOToTest.setIdOfJobs(id);
        transactionCreationDTOToTest.setIdOfProducts(id);
        Customer customerFromService = new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl");
        when(customerRepository.findById(transactionCreationDTOToTest.getIdCustomer())).thenReturn(Optional.of(customerFromService));
        Job jobFromService = new Job("name", "price");
        Product productFromService = new Product("name", 45.50, 5.0);
        List<Job> jobsFromService = new LinkedList<>();
        jobsFromService.add(jobFromService);
        List<Product> productsFromService = new LinkedList<>();
        productsFromService.add(productFromService);
        when(jobRepository.findAllByIdJobIn(transactionCreationDTOToTest.getIdOfJobs())).thenReturn(jobsFromService);
        when(productRepository.findAllByIdProductIn(transactionCreationDTOToTest.getIdOfProducts())).thenReturn(productsFromService);
        transactionService.addTransaction(transactionCreationDTOToTest);
    }

    @Test
    public void testAddTransactionExceptionThrown() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Long> id = new LinkedList<>();
        id.add(5L);
        TransactionCreationDTO transactionCreationDTOToTest = new TransactionCreationDTO();
        transactionCreationDTOToTest.setPrice(45.50);
        transactionCreationDTOToTest.setDescription("description");
        transactionCreationDTOToTest.setIdCustomer(10L);
        transactionCreationDTOToTest.setIdOfJobs(id);
        transactionCreationDTOToTest.setIdOfProducts(id);
        when(customerRepository.findById(transactionCreationDTOToTest.getIdCustomer())).thenThrow(new NoSuchElementException("Klient o takim ID nie istnieje!"));
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> transactionService.addTransaction(transactionCreationDTOToTest));
        assertEquals(exception.getMessage(), "Klient o takim ID nie istnieje!");
    }

    @Test
    public void testAddTransactionExceptionThrown2() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Long> id = new LinkedList<>();
        id.add(5L);
        TransactionCreationDTO transactionCreationDTOToTest = new TransactionCreationDTO();
        transactionCreationDTOToTest.setPrice(45.50);
        transactionCreationDTOToTest.setDescription("description");
        transactionCreationDTOToTest.setIdCustomer(10L);
        transactionCreationDTOToTest.setIdOfJobs(id);
        transactionCreationDTOToTest.setIdOfProducts(id);
        Customer customerFromService = new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl");
        when(customerRepository.findById(transactionCreationDTOToTest.getIdCustomer())).thenReturn(Optional.of(customerFromService));
        when(jobRepository.findAllByIdJobIn(transactionCreationDTOToTest.getIdOfJobs())).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> transactionService.addTransaction(transactionCreationDTOToTest));
        assertEquals(exception.getMessage(), "Usługi o takich ID nie istnieją!");
    }

    @Test
    public void testAddTransactionExceptionThrown3() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Long> id = new LinkedList<>();
        id.add(5L);
        TransactionCreationDTO transactionCreationDTOToTest = new TransactionCreationDTO();
        transactionCreationDTOToTest.setPrice(45.50);
        transactionCreationDTOToTest.setDescription("description");
        transactionCreationDTOToTest.setIdCustomer(10L);
        transactionCreationDTOToTest.setIdOfJobs(id);
        transactionCreationDTOToTest.setIdOfProducts(id);
        Customer customerFromService = new Customer("Jan", "Kowalski", "Warszawa", "123456789", "jan.kowalski@email.pl");
        when(customerRepository.findById(transactionCreationDTOToTest.getIdCustomer())).thenReturn(Optional.of(customerFromService));
        Job jobFromService = new Job("name", "price");
        List<Job> jobsFromService = new LinkedList<>();
        jobsFromService.add(jobFromService);
        when(jobRepository.findAllByIdJobIn(transactionCreationDTOToTest.getIdOfJobs())).thenReturn(jobsFromService);
        when(productRepository.findAllByIdProductIn(transactionCreationDTOToTest.getIdOfProducts())).thenReturn(null);
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> transactionService.addTransaction(transactionCreationDTOToTest));
        assertEquals(exception.getMessage(), "Produkty o takich ID nie istnieją!");
    }
}
