package br.com.fiap.ecopark.infrastructure.api.rest.exception;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotAuthorizedException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Chave de API inválida!")).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("message", "Ops, aconteceu um erro inesperado!")).build();
    }
}
