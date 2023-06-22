package com.example.soleproprietorship.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/***
 * Kontroler dotyczacy klienta.
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /***
     * Metoda zwracajaca DTO klienta dla podanego ID Klienta.
     * @param idCustomer ID Klienta
     * @return DTO Klienta
     */
    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomer(@RequestParam Long idCustomer) {
        return new ResponseEntity<>(customerService.getCustomer(idCustomer), HttpStatus.OK);
    }

    /***
     * Metoda zwracajaca liste DTO klientow posiadanych przez danego uzytkownika.
     * @return Lista DTO klientow
     */
    @GetMapping("/user")
    public ResponseEntity<List<CustomerDTO>> getUserCustomers() {
        return new ResponseEntity<>(customerService.getUserCustomers(), HttpStatus.OK);
    }

    /***
     * Metoda sluzaca dodaniu klienta do systemu.
     * @param dto DTO klienta
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerCreationDTO dto) {
        customerService.addCustomer(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca edycji klienta w systemie.
     * @param dto DTO klienta
     * @return
     */
    @PatchMapping
    public ResponseEntity<Void> editCustomer(@Valid @RequestBody CustomerDTO dto) {
        customerService.editCustomer(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /***
     * Metoda sluzaca usunieciu klienta z systemu.
     * @param idCustomer ID klienta
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer(@RequestParam Long idCustomer) {
        customerService.deleteCustomer(idCustomer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
