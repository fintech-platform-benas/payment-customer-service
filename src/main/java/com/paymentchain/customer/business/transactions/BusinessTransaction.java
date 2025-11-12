package com.paymentchain.customer.business.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.clients.ProductClient;
import com.paymentchain.customer.clients.TransactionClient;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.respository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de lógica de negocio para Customer.
 * 
 * Responsabilidades:
 * - Orquestar llamadas a Product y Transaction services
 * - Enriquecer datos de customer con info externa
 * - Validar reglas de negocio
 * 
 * Cambios respecto a versión anterior:
 * - Usa HTTP Interfaces en lugar de WebClient manual
 * - Mejor manejo de errores (fallbacks, timeouts)
 * - Más fácil de testear (inyección de dependencias clara)
 * 
 * @author benas
 */
@Service
public class BusinessTransaction {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessTransaction.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductClient productClient;
    
    @Autowired
    private TransactionClient transactionClient;
    
    /**
     * Obtiene customer completo con productos y transacciones enriquecidos.
     * 
     * Flujo:
     * 1. Busca customer en base de datos local
     * 2. Para cada producto → llama a product service y obtiene nombre
     * 3. Llama a transaction service y obtiene transacciones del IBAN
     * 4. Retorna customer enriquecido
     * 
     * Manejo de errores:
     * - Si product service falla → nombre queda null (degradación graciosa)
     * - Si transaction service falla → lista vacía (no bloquea respuesta)
     * 
     * @param code Código único del customer
     * @return Customer enriquecido o null si no existe
     */
    public Customer get(String code) {
        LOGGER.debug("Fetching customer with code: {}", code);
        
        Customer customer = customerRepository.findByCode(code);
        
        if (customer == null) {
            LOGGER.warn("Customer not found: {}", code);
            return null;
        }
        
        if (customer.getProducts() == null || customer.getProducts().isEmpty()) {
            LOGGER.debug("Customer {} has no products", code);
            return customer;
        }
        
        // Enriquece productos con nombres desde product service
        LOGGER.debug("Enriching {} products for customer {}", 
            customer.getProducts().size(), code);
        
        customer.getProducts().forEach(this::enrichProductWithName);
        
        // Obtiene transacciones desde transaction service
        LOGGER.debug("Fetching transactions for IBAN: {}", customer.getIban());
        List<?> transactions = getTransactions(customer.getIban());
        customer.setTransitions(transactions);
        
        LOGGER.info("Customer {} enriched successfully: {} products, {} transactions",
            code, customer.getProducts().size(), transactions.size());
        
        return customer;
    }
    
    /**
     * Enriquece producto con nombre desde product service.
     * 
     * Estrategia de fallback:
     * - Timeout: 3 segundos
     * - Si falla: nombre = null (no bloquea flujo)
     * 
     * @param customerProduct Producto a enriquecer (se modifica in-place)
     */
    private void enrichProductWithName(CustomerProduct customerProduct) {
        try {
            LOGGER.debug("Fetching product name for ID: {}", customerProduct.getProductId());
            
            String productName = productClient
                .getProduct(customerProduct.getProductId())
                .map(json -> json.get("name").asText())
                .timeout(Duration.ofSeconds(3))
                .onErrorReturn(null) // Fallback: si falla, continúa sin nombre
                .block(); // OK bloquear aquí: estamos en contexto síncrono (forEach)
            
            customerProduct.setProductName(productName);
            
            if (productName == null) {
                LOGGER.warn("Product name not found for ID: {}", customerProduct.getProductId());
            }
            
        } catch (Exception ex) {
            LOGGER.error("Failed to enrich product {}: {}", 
                customerProduct.getProductId(), ex.getMessage());
            customerProduct.setProductName(null);
        }
    }
    
    /**
     * Obtiene transacciones desde transaction service.
     * 
     * Estrategia de fallback:
     * - Timeout: 3 segundos
     * - Si falla: retorna lista vacía
     * 
     * @param iban IBAN de la cuenta
     * @return Lista de transacciones (JSON) o lista vacía
     */
    private List<?> getTransactions(String iban) {
        try {
            List<?> transactions = transactionClient
                .getTransactionsByIban(iban)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .onErrorReturn(Collections.emptyList())
                .block();
            
            LOGGER.debug("Fetched {} transactions for IBAN: {}", 
                transactions.size(), iban);
            
            return transactions;
            
        } catch (Exception ex) {
            LOGGER.error("Failed to fetch transactions for IBAN {}: {}", 
                iban, ex.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Crea nuevo customer validando que productos referenciados existan.
     * 
     * Validación:
     * - Por cada producto en input → verifica que existe en product service
     * - Si alguno no existe → BusinessRuleException (HTTP 412)
     * 
     * @param input Customer a crear
     * @return Customer guardado
     * @throws BusinessRuleException Si algún producto referenciado no existe
     */
    public Customer post(Customer input) throws BusinessRuleException {
        LOGGER.debug("Creating customer: {}", input.getName());
        
        if (Objects.nonNull(input.getProducts())) {
            
            for (CustomerProduct productDTO : input.getProducts()) {
                LOGGER.debug("Validating product: {}", productDTO.getProductId());
                
                String productName = getProductNameForValidation(productDTO.getProductId());
                
                if (productName == null) {
                    LOGGER.error("Product validation failed: product {} does not exist", 
                        productDTO.getProductId());
                    
                    throw new BusinessRuleException(
                        1025,
                        "Product with ID " + productDTO.getProductId() + " does not exist",
                        HttpStatus.PRECONDITION_FAILED
                    );
                }
                
                LOGGER.debug("Product {} validated: {}", productDTO.getProductId(), productName);
            }
        }
        
        Customer saved = customerRepository.save(input);
        LOGGER.info("Customer created successfully: ID={}, code={}", 
            saved.getId(), saved.getCode());
        
        return saved;
    }
    
    /**
     * Obtiene nombre de producto (solo para validación).
     * 
     * Diferencia con enrichProductWithName:
     * - Aquí NO queremos fallback (si falla, debe lanzar error)
     * - Si producto no existe → retorna null (para lanzar BusinessRuleException)
     * 
     * @param productId ID del producto
     * @return Nombre del producto o null si no existe
     */
    private String getProductNameForValidation(Long productId) {
        try {
            return productClient
                .getProduct(productId)
                .map(json -> json.get("name").asText())
                .timeout(Duration.ofSeconds(3))
                .block();
                
        } catch (Exception ex) {
            LOGGER.error("Product validation call failed for ID {}: {}", 
                productId, ex.getMessage());
            return null;
        }
    }
}