package com.paymentchain.customer.config;

import com.paymentchain.customer.clients.ProductClient;
import com.paymentchain.customer.clients.TransactionClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

/**
 * Test configuration that provides mocked HTTP clients.
 * This prevents the need for LoadBalancer in tests.
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Primary
    public ProductClient productClient() {
        return mock(ProductClient.class);
    }

    @Bean
    @Primary
    public TransactionClient transactionClient() {
        return mock(TransactionClient.class);
    }
}
