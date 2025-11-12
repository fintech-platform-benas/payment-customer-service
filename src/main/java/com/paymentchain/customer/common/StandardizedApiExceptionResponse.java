package com.paymentchain.customer.common;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "ProblemDetails",
        description = "Respuesta de error conforme al estándar RFC 7807 Problem Details for HTTP APIs",
        example = """
    {
      "type": "https://example.com/errors/not-found",
      "title": "Recurso no encontrado",
      "status": 404,
      "detail": "El recurso con ID 123 no fue encontrado",
      "instance": "/api/v1/recurso/123"
    }
    """
)
@NoArgsConstructor
@Data
public class StandardizedApiExceptionResponse {

    @Schema(
            name = "type",
            description = "URI que identifica el tipo de error. Puede apuntar a documentación del error.",
            example = "https://example.com/errors/not-found",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String type;

    @Schema(
            name = "title",
            description = "Título corto legible para humanos que resume el error.",
            example = "Recurso no encontrado",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @Schema(
            name = "status",
            description = "Código de estado HTTP que describe esta ocurrencia del problema. Debe coincidir con el código de estado de la respuesta.",
            example = "404",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int status;

    @Schema(
            name = "detail",
            description = "Descripción detallada y específica del error.",
            example = "El recurso con ID 123 no fue encontrado",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String detail;

    @Schema(
            name = "instance",
            description = "URI que identifica esta instancia particular del problema.",
            example = "/api/v1/recurso/123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String instance;

    public StandardizedApiExceptionResponse(String type, String title, int status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }
}
