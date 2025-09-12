package br.com.fiap.ecopark.domain.model;

import br.com.fiap.ecopark.domain.exceptions.ValidacaoDominioException;

public class Carro {

    private String marca;

    private String modelo;

    private Integer ano;

    private String placa;

    private CarroPorte porte;
    public Carro(String marca, String modelo, Integer ano, String placa, CarroPorte porte) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        setPlaca(placa);
        this.porte = porte;

    }

    private void setPlaca(String placa) {
        this.placa = placa;
        isPlacaValida();
    }

    private void isPlacaValida() {
        //regex para placa mercosul
        if(!this.placa.matches("[A-Z]{3}[0-9]{4}")){
            throw new ValidacaoDominioException("Placa inválida");
        }
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public String getPlaca() {
        return placa;
    }

    public CarroPorte getPorte() {
        return porte;
    }

}
