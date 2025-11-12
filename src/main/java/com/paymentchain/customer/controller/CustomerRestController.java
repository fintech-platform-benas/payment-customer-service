package com.paymentchain.customer.controller;

import com.paymentchain.customer.business.transactions.BusinessTransaction;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.respository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

/**
 * @author benas
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    Logger LOGGER = LoggerFactory.getLogger(CustomerRestController.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BusinessTransaction businessTransaction;

    @Autowired
    private Environment env;

    @GetMapping("/check")
    public String check() {
        return "Your property value is: " + env.getProperty("custom.activeprofileName");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(name = "id") long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> list() {
        List<Customer> customerList = customerRepository.findAll();
        if (customerList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(customerList);
        }
    }

    @GetMapping("/full")
    public ResponseEntity<?> getByCode(@RequestParam(name = "code") String code) {

        Customer customer = businessTransaction.get(code);

        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer input) throws BusinessRuleException, UnknownHostException {
        Customer save = businessTransaction.post(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable("id") long id, @RequestBody Customer input) {
        Optional<Customer> optionalcustomer = customerRepository.findById(id);
        if (optionalcustomer.isPresent()) {
            Customer newcustomer = optionalcustomer.get();
            newcustomer.setName(input.getName());
            newcustomer.setPhone(input.getPhone());
            Customer save = customerRepository.save(newcustomer);
            return ResponseEntity.ok(save);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
