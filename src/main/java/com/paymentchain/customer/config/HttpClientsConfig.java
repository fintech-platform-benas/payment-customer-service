package com.paymentchain.customer.config;

import com.paymentchain.customer.clients.ProductClient;
import com.paymentchain.customer.clients.TransactionClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Configuración de clientes HTTP para comunicación entre microservicios.
 * 
 * Usa HTTP Interfaces (Spring Boot 3) en lugar de Feign tradicional.
 * 
 * @author benas
 */
@Configuration
public class HttpClientsConfig {
    
    /**
     * HttpClient base con timeouts configurados.
     * 
     * Configuración de timeouts:
     * - Connection timeout: 5s (tiempo para establecer conexión TCP)
     * - Response timeout: 5s (tiempo máximo esperando respuesta completa)
     * - Read timeout: 5s (tiempo entre bytes leídos)
     * - Write timeout: 5s (tiempo para escribir request)
     * 
     * Estos valores evitan que requests colgados bloqueen el sistema.
     */
    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofSeconds(5))
            .doOnConnected(conn -> {
                conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                conn.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
    }
    
    /**
     * WebClient.Builder con Load Balancer habilitado.
     * 
     * @LoadBalanced: permite usar nombres de servicio de Eureka
     * Ejemplo: "http://BUSINESSDOMAIN-PRODUCT" → Eureka resuelve la IP real
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(HttpClient httpClient) {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
    
    /**
     * Cliente para Product Service.
     * 
     * Pasos:
     * 1. Crea WebClient con baseUrl del servicio
     * 2. Crea factory que genera implementación de interfaces
     * 3. Retorna proxy de ProductClient
     */
    @Bean
    public ProductClient productClient(WebClient.Builder builder) {
        WebClient webClient = builder
            .baseUrl("http://BUSINESSDOMAIN-PRODUCT")
            .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient)) // ✅ usa builderFor + create()
            .build();

        return factory.createClient(ProductClient.class);
    }

    
    /**
     * Cliente para Transaction Service.
     */
    @Bean
    public TransactionClient transactionClient(WebClient.Builder builder) {
        WebClient webClient = builder
            .baseUrl("http://BUSINESSDOMAIN-TRANSACTION")
            .build();
        
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        
        return factory.createClient(TransactionClient.class);
    }
}