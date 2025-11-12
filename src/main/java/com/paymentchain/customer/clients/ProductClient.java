package com.paymentchain.customer.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

/**
 * Cliente HTTP declarativo para Product Service.
 * 
 * Reemplaza WebClient manual con interfaz declarativa (estilo Feign).
 * 
 * Ventajas:
 * - Spring genera implementación automáticamente
 * - Fácil mockear en tests (solo mockeas la interfaz)
 * - Código más limpio y legible
 * 
 * @author benas
 */
@HttpExchange("/product")
public interface ProductClient {
    
    /**
     * Obtiene producto por ID.
     * 
     * @param id ID del producto
     * @return Mono con JSON del producto
     */
    @GetExchange("/{id}")
    Mono<JsonNode> getProduct(@PathVariable Long id);
}