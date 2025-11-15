package com.paymentchain.customer;

import com.paymentchain.customer.clients.ProductClient;
import com.paymentchain.customer.clients.TransactionClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.netty.http.client.HttpClient;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.NONE,
	properties = {
		"eureka.client.enabled=false",
		"spring.cloud.config.enabled=false",
		"spring.cloud.loadbalancer.enabled=false"
	}
)
@ActiveProfiles("test")
class CustomerApplicationTests {

	@MockBean
	private ProductClient productClient;

	@MockBean
	private TransactionClient transactionClient;

	@MockBean
	private HttpClient httpClient;

	@Test
	void contextLoads() {
		// Test passes if Spring context loads successfully
	}

}
