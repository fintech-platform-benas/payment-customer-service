package com.paymentchain.customer;

import com.paymentchain.customer.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.NONE,
	properties = {
		"eureka.client.enabled=false",
		"spring.cloud.config.enabled=false",
		"spring.cloud.loadbalancer.enabled=false"
	}
)
@Import(TestConfig.class)
@ActiveProfiles("test")
class CustomerApplicationTests {

	@Test
	void contextLoads() {
		// Test passes if Spring context loads successfully
	}

}
