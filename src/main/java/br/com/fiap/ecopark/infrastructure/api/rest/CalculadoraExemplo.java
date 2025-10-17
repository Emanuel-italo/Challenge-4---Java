package br.com.fiap.ecopark.infrastructure.api.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/calculadora")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalculadoraExemplo {


    @POST
    @Path("/calcule")
    public Response calcular(InformacoesParaCalculo informacoesParaCalculo) {
        Float resultado = calcular(informacoesParaCalculo.getValor1(),
                informacoesParaCalculo.getValor2(),
                informacoesParaCalculo.getOperacao());

        ResultadoCalculadora resultadoCalculadora = new ResultadoCalculadora(resultado);
        return Response.status(Response.Status.OK).entity(resultadoCalculadora).build();
    }

    public Float calcular(Float valor1, Float valor2, String operacao){
        if (operacao.equals("*")) {
            return valor1 * valor2;
        } else if (operacao.equals("/")){
            return valor1 / valor2;
        } else if (operacao.equals("+")) {
            return  valor1 + valor2;
        } else if (operacao.equals("-")) {
            return valor1 - valor2;
        } else {
            throw new RuntimeException("Operação invalida");
        }
    }
}
