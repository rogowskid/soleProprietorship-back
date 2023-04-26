package com.example.soleproprietorship.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomer(@RequestParam Long idCustomer) {
        return new ResponseEntity<>(customerService.getCustomer(idCustomer), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CustomerDTO>> getUserCustomers() {
        return new ResponseEntity<>(customerService.getUserCustomers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerCreationDTO dto) {
        customerService.addCustomer(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> editCustomer(@Valid @RequestBody CustomerDTO dto) {
        customerService.editCustomer(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer(@RequestParam Long idCustomer) {
        customerService.deleteCustomer(idCustomer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
