package com.example.soleproprietorship.transaction;

import com.example.soleproprietorship.common.EntityDTO;
import com.example.soleproprietorship.common.EntityModelValid;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.customer.Customer;
import com.example.soleproprietorship.customer.CustomerRepository;
import com.example.soleproprietorship.job.Job;
import com.example.soleproprietorship.job.JobRepository;
import com.example.soleproprietorship.product.Product;
import com.example.soleproprietorship.product.ProductRepository;
import com.example.soleproprietorship.user.User;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TransactionService extends EntityDTO<Transaction, TransactionCreationDTO, TransactionDTO>
        implements EntityModelValid<Transaction, Long> {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MyUserDetailsService userDetailsService;

    public TransactionDTO getTransaction(Long idTransaction) {
        User user = userDetailsService.getUserFromToken();
        Transaction transaction = repository.findByIdTransactionAndUser(idTransaction, user);
        if (transaction == null) {
            throw new NoSuchElementException("Transakcja nie istnieje!");
        }
        return mapEntityToSingleDTO(transaction);
    }

    public List<TransactionDTO> getUserTransactions() {
        User user = userDetailsService.getUserFromToken();
        List<Transaction> transactions = repository.findAllByUser(user);
        if (transactions == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych transakcji!");
        }
        return transactions.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void addTransaction(TransactionCreationDTO dto) {
        User user = userDetailsService.getUserFromToken();
        Transaction transaction = mapCreationDTOToEntity(dto);
        Customer customer = customerRepository.findById(dto.getIdCustomer())
                .orElseThrow(() -> new NoSuchElementException("Klient o takim ID nie istnieje!"));
        List<Job> jobs = jobRepository.findAllByIdJobIn(dto.getIdOfJobs());
        if (jobs == null) {
            throw new NoSuchElementException("Usługi o takich ID nie istnieją!");
        }
        List<Product> products = productRepository.findAllByIdProductIn(dto.getIdOfProducts());
        if (products == null) {
            throw new NoSuchElementException("Produkty o takich ID nie istnieją!");
        }
        transaction.setUser(user);
        transaction.setJobs(jobs);
        transaction.setProducts(products);
        repository.save(transaction);
    }

    private TransactionDTO mapEntityToSingleDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getIdTransaction(), transaction.getDate(), transaction.getPrice(),
                transaction.getDescription(), transaction.getCustomer().getName() + transaction.getCustomer().getSurName(),
                transaction.getProducts().size(), transaction.getJobs().size(), transaction.getProducts(), transaction.getJobs());
    }

    @Override
    protected TransactionDTO mapEntityToDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getIdTransaction(), transaction.getDate(), transaction.getPrice(),
                transaction.getDescription(), transaction.getCustomer().getName() + " " + transaction.getCustomer().getSurName(),
                transaction.getProducts().size(), transaction.getJobs().size());
    }

    @Override
    protected Transaction mapCreationDTOToEntity(TransactionCreationDTO dto) {
        return new Transaction(dto.getDate(), dto.getPrice(), dto.getDescription());
    }

    @Override
    public Transaction executeEncode(Transaction entity) {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(transaction.getIdTransaction());
        transaction.setDescription(Encode.forHtml(entity.getDescription()));
        transaction.setPrice(entity.getPrice());
        transaction.setDate(entity.getDate());
        transaction.setUser(entity.getUser());
        transaction.setJobs(entity.getJobs());
        transaction.setCustomer(entity.getCustomer());
        transaction.setProducts(entity.getProducts());
        return transaction;
    }

    @Override
    public List<Transaction> executeEncodeList(List<Transaction> entities){
        ArrayList<Transaction> transactions = new ArrayList<>();

        for (Transaction entity : entities) {
            transactions.add(executeEncode(entity));
        }

        return transactions;

    }

    @Override
    public Transaction getEntity(Long idTransaction) {
        User user = userDetailsService.getUserFromToken();
        Transaction transaction = repository.findByIdTransactionAndUser(idTransaction, user);
        if (transaction == null) {
            throw new NoSuchElementException("Transakcja nie istnieje!");
        }
        return transaction;
    }

    @Override
    public List<Transaction> getEntities() {
        User user = userDetailsService.getUserFromToken();
        List<Transaction> transactions = repository.findAllByUser(user);
        if (transactions == null) {
            throw new NoSuchElementException("Użytkownik nie posiada żadnych transakcji!");
        }
        return transactions;
    }
}
