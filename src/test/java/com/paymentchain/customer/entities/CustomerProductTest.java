package com.paymentchain.customer.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerProductTest {

    @Test
    void testCustomerProductEntity() {
        // Given
        CustomerProduct customerProduct = new CustomerProduct();
        Customer customer = new Customer();
        customer.setId(1L);

        // When
        customerProduct.setId(1L);
        customerProduct.setProductId(100L);
        customerProduct.setProductName("Test Product");
        customerProduct.setCustomer(customer);

        // Then
        assertEquals(1L, customerProduct.getId());
        assertEquals(100L, customerProduct.getProductId());
        assertEquals("Test Product", customerProduct.getProductName());
        assertNotNull(customerProduct.getCustomer());
        assertEquals(1L, customerProduct.getCustomer().getId());
    }

    @Test
    void testCustomerProductWithoutCustomer() {
        // Given
        CustomerProduct customerProduct = new CustomerProduct();

        // When
        customerProduct.setId(1L);
        customerProduct.setProductId(200L);

        // Then
        assertEquals(1L, customerProduct.getId());
        assertEquals(200L, customerProduct.getProductId());
        assertNull(customerProduct.getCustomer());
    }
}
