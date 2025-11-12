package com.paymentchain.customer.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

/**
 * Cliente HTTP declarativo para Transaction Service.
 * 
 * @author benas
 */
@HttpExchange("/transactions")
public interface TransactionClient {
    
    /**
     * Obtiene transacciones por IBAN.
     * 
     * @param iban IBAN de la cuenta
     * @return Flux de transacciones en formato JSON
     */
    @GetExchange("/transaction")
    Flux<JsonNode> getTransactionsByIban(@RequestParam("accountIban") String iban);
}