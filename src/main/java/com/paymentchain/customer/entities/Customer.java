package com.paymentchain.customer.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @author benas
 */
@Entity
@Data
public class Customer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @Column(name = "code")
    private String code;
    private String name;
    private String surname;
    private String address;
    private String phone;
    private String iban;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;
    @Transient
    private List<?> transitions;


}
