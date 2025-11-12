package com.paymentchain.customer.exception;

import com.paymentchain.customer.common.StandardizedApiExceptionResponse;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.UnknownHostException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardizedApiExceptionResponse> handleBusinessRuleException(
            BusinessRuleException ex,
            WebRequest request

    ) {
        StandardizedApiExceptionResponse standardizedApiExceptionResponse = new StandardizedApiExceptionResponse(
                "BUSINESS",
                "ERROR VALIDATION " + ex.getMessage(),
                ex.getHttpStatus().value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
                .body(standardizedApiExceptionResponse);
    }


    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<StandardizedApiExceptionResponse> handleUnknownHostException(
            UnknownHostException ex,
            WebRequest request
    ) {
        StandardizedApiExceptionResponse response = new StandardizedApiExceptionResponse(
                "TECHNIC",
                "No se pudo contactar con el servicio remoto",
                HttpStatus.BAD_GATEWAY.value(),
                "No se pudo resolver el nombre del host remoto. Verifique la configuración del DNS o el nombre del servicio: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
                .body(response);
    }


    /**
     * Manejo global de excepciones no controladas

    //@ExceptionHandler(Exception.class)
    public ResponseEntity<StandardizedApiExceptionResponse> handleAllUncaught(
            Exception ex,
            WebRequest request
    ) {
        StandardizedApiExceptionResponse problem = new StandardizedApiExceptionResponse(
                "about:blank",
                "Error interno del servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
                .body(problem);
    }
     */

    @ExceptionHandler(OpenApiResourceNotFoundException.class)
    public ResponseEntity<StandardizedApiExceptionResponse> handleResourceNotFound(
            OpenApiResourceNotFoundException ex,
            WebRequest request
    ) {
        StandardizedApiExceptionResponse problem = new StandardizedApiExceptionResponse(
                "https://example.com/errors/not-found",
                "Recurso no encontrado",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
                .body(problem);
    }

}
