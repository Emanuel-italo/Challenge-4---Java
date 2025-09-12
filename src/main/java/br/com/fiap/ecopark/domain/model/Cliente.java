package br.com.fiap.ecopark.domain.model;

import br.com.fiap.ecopark.domain.exceptions.ValidacaoDominioException;

import java.time.LocalDate;
import java.util.Objects;

public class Cliente {

    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Endereco endereco;
    private Integer anoNascimento;
    private Boolean ativo;
    private Long versao;

    public Cliente(String nome, String cpf, Integer anoNascimento, String telefone, String email, Endereco endereco, Boolean ativo, Long versao) {
        setNome(nome);
        setCpf(cpf);
        setAnoNascimento(anoNascimento);
        setTelefone(telefone);
        setEmail(email);
        setEndereco(endereco);
        this.ativo = ativo;
        setVersao(versao);
    }

    public Cliente(String nome, String cpf, Boolean ativo) {
        setNome(nome);
        setCpf(cpf);
        this.ativo = ativo;
    }

    private void setNome(String nome) {
        this.nome = nome;
        isNomeValido();
    }

    private void isNomeValido() {
        //valida se nome é nulo, vazio ou em branco
        if (nome == null || nome.isEmpty() || nome.isBlank()) {
            throw new ValidacaoDominioException("Nome vazio");
        }

        //valida se nome tem no mínimo duas palavras e cada palavra pelo menos 3 caracteres
        String[] palavras = nome.trim().split("\\s+");
        if (palavras.length < 2 || palavras[0].length() < 3 || palavras[1].length() < 3) {
            throw new ValidacaoDominioException("Nome deve ter pelo menos duas palavras e cada palavra pelo menos 3 caracteres");
        }
    }

    private void setCpf(String cpf) {
        // Remove caracteres que não sejam números
        this.cpf = cpf.replaceAll("\\D", "");
        isCpfValido(this.cpf);
    }

    public static void isCpfValido(String cpf) {
        //regex valida se cpf só contem numeros
        if (!cpf.matches("\\d{11}")) {
            throw new ValidacaoDominioException("CPF deve conter apenas números");
        }

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            throw new ValidacaoDominioException("CPF deve ter 11 dígitos");
        }

        // Elimina CPFs com todos os dígitos iguais (ex: 00000000000, 11111111111, etc.)
        if (cpf.matches("(\\d)\\1{10}")) {
            throw new ValidacaoDominioException("CPF inválido por ter todos os dígitos iguais");
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = 11 - (soma % 11);
        int digito1 = (resto == 10 || resto == 11) ? 0 : resto;

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = 11 - (soma % 11);
        int digito2 = (resto == 10 || resto == 11) ? 0 : resto;

        // Confere se os dígitos calculados são iguais aos do CPF informado
        boolean valido = digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');

        if (!valido) {
            throw new ValidacaoDominioException("CPF inválido");
        }

    }

    private void setTelefone(String telefone) {
        this.telefone = telefone;
        isTelefoneValido();
    }

    private void isTelefoneValido() {
        //regex para validar se telefone (xx)xxxxx-xxxx
        final String regex = "^\\(\\d{2}\\)\\d{4,5}-\\d{4}$";
        if (!telefone.matches(regex)) {
            throw new ValidacaoDominioException("Telefone inválido, utilize o formato (DD) XXXXX-XXXX");
        }
    }

    private void setEmail(String email) {
        this.email = email;
        isEmailValido();
    }

    private void isEmailValido() {
        final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(regex)) {
            throw new ValidacaoDominioException("Email inválido");
        }
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        isEnderecoValido();
    }

    private void isEnderecoValido() {
        if (endereco == null) {
            throw new ValidacaoDominioException("É necessário informar um endereço");
        }
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
        isAnoNascimentoValido();
    }

    private void isAnoNascimentoValido() {
        if (anoNascimento == null) {
            throw new ValidacaoDominioException("É necessário informar o ano de nascimento");
        }

        if (LocalDate.now().getYear() - this.anoNascimento < 18) {
            throw new ValidacaoDominioException("Cliente deve ter pelo menos 18 anos");
        }
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Long getVersao() {
        return versao;
    }

    private void setVersao(Long versao) {
        this.versao = Objects.requireNonNullElse(versao, 0L);
    }

    public void incrementarVersao() {
        if (versao == null) {
            versao = 0L;
        } else {
            versao++;
        }
    }
}
