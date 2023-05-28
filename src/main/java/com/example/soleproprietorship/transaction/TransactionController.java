package com.example.soleproprietorship.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(name = "/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<TransactionDTO> getTransaction(@RequestParam Long idTransaction) {
        return new ResponseEntity<>(transactionService.getTransaction(idTransaction), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions() {
        return new ResponseEntity<>(transactionService.getUserTransactions(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionCreationDTO dto) {
        transactionService.addTransaction(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
