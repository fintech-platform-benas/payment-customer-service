package com.paymentchain.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author benas
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessRuleException extends Throwable {

    private long id;
    private String code;
    private HttpStatus httpStatus;

    public BusinessRuleException(long id, String code, HttpStatus httpStatus) {
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessRuleException(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
