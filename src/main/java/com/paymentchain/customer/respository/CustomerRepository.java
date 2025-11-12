package com.paymentchain.customer.respository;

import com.paymentchain.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author benas
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.code = :code")
    Customer findByCode(@Param("code") String code);

    //@Query("SELECT IBAN FROM CUSTOMER WHERE IBAN = ?1")
    //public Customer findByAccount(String iban);
}
