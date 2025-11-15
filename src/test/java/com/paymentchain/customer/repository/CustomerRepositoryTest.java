package com.paymentchain.customer.repository;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.respository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testFindById() {
        // Given
        Customer customer = new Customer();
        customer.setCode("CUST001");
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setAddress("123 Main St");
        customer.setPhone("+1234567890");
        customer.setIban("ES1234567890");

        Customer savedCustomer = entityManager.persistAndFlush(customer);

        // When
        Optional<Customer> found = customerRepository.findById(savedCustomer.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("CUST001", found.get().getCode());
        assertEquals("John", found.get().getName());
        assertEquals("Doe", found.get().getSurname());
    }

    @Test
    void testFindByCode() {
        // Given
        Customer customer = new Customer();
        customer.setCode("CUST002");
        customer.setName("Jane");
        customer.setSurname("Smith");
        customer.setAddress("456 Oak Ave");
        customer.setPhone("+0987654321");
        customer.setIban("ES0987654321");

        entityManager.persistAndFlush(customer);

        // When
        Customer found = customerRepository.findByCode("CUST002");

        // Then
        assertNotNull(found);
        assertEquals("CUST002", found.getCode());
        assertEquals("Jane", found.getName());
        assertEquals("Smith", found.getSurname());
    }

    @Test
    void testFindByCodeNotFound() {
        // When
        Customer found = customerRepository.findByCode("NONEXISTENT");

        // Then
        assertNull(found);
    }

    @Test
    void testSaveCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setCode("CUST003");
        customer.setName("Bob");
        customer.setSurname("Johnson");
        customer.setAddress("789 Elm St");
        customer.setPhone("+1122334455");
        customer.setIban("ES1122334455");

        // When
        Customer saved = customerRepository.save(customer);

        // Then
        assertNotNull(saved.getId());
        assertEquals("CUST003", saved.getCode());
    }

    @Test
    void testDeleteCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setCode("CUST004");
        customer.setName("Alice");
        customer.setSurname("Williams");

        Customer saved = entityManager.persistAndFlush(customer);
        Long customerId = saved.getId();

        // When
        customerRepository.deleteById(customerId);
        entityManager.flush();

        // Then
        Optional<Customer> found = customerRepository.findById(customerId);
        assertFalse(found.isPresent());
    }
}
