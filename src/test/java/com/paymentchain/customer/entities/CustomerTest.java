package com.paymentchain.customer.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerEntity() {
        // Given
        Customer customer = new Customer();

        // When
        customer.setId(1L);
        customer.setCode("CUST001");
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setAddress("123 Main St");
        customer.setPhone("+1234567890");
        customer.setIban("ES1234567890");

        List<CustomerProduct> products = new ArrayList<>();
        customer.setProducts(products);

        // Then
        assertEquals(1L, customer.getId());
        assertEquals("CUST001", customer.getCode());
        assertEquals("John", customer.getName());
        assertEquals("Doe", customer.getSurname());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("+1234567890", customer.getPhone());
        assertEquals("ES1234567890", customer.getIban());
        assertNotNull(customer.getProducts());
        assertEquals(0, customer.getProducts().size());
    }

    @Test
    void testCustomerWithProducts() {
        // Given
        Customer customer = new Customer();
        CustomerProduct product1 = new CustomerProduct();
        product1.setId(1L);
        product1.setProductId(100L);
        product1.setCustomer(customer);

        CustomerProduct product2 = new CustomerProduct();
        product2.setId(2L);
        product2.setProductId(200L);
        product2.setCustomer(customer);

        List<CustomerProduct> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        // When
        customer.setProducts(products);

        // Then
        assertEquals(2, customer.getProducts().size());
        assertEquals(100L, customer.getProducts().get(0).getProductId());
        assertEquals(200L, customer.getProducts().get(1).getProductId());
    }

    @Test
    void testTransitionsField() {
        // Given
        Customer customer = new Customer();
        List<Object> transitions = new ArrayList<>();

        // When
        customer.setTransitions(transitions);

        // Then
        assertNotNull(customer.getTransitions());
        assertEquals(0, customer.getTransitions().size());
    }
}
