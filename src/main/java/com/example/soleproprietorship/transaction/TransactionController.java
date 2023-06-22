package com.example.soleproprietorship.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 * Kontroler dotyczacy transakcji.
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /***
     * Metoda zwracajaca DTO Transakcji na podstawie ID Transakcji.
     * @param idTransaction ID Transakcji.
     * @return DTO Transakcji.
     */
    @GetMapping
    public ResponseEntity<TransactionDTO> getTransaction(@RequestParam Long idTransaction) {
        return new ResponseEntity<>(transactionService.getTransaction(idTransaction), HttpStatus.OK);
    }

    /***
     * Metoda zwracajaca liste DTO Transakcji uzytkownika.
     * @return Lista DTO transakcji uzytkownika.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions() {
        return new ResponseEntity<>(transactionService.getUserTransactions(), HttpStatus.OK);
    }

    /***
     * Metoda umozliwiajaca dodanie transakcji do systemu.
     * @param dto DTO Transakcji
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionCreationDTO dto) {
        transactionService.addTransaction(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
