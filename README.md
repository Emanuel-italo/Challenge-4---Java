# Projeto EcoPark - Guia de Implementação Passo a Passo

## Atividade 1: Implementação do Domain Model

### Objetivo
Implementar todas as classes e interfaces necessárias para a camada de domínio do modelo, estabelecendo as entidades principais do sistema EcoPark.

### Instruções Detalhadas
1. Crie um projeto Maven utilizando Quarkus como framework base.
   ```bash
   mvn io.quarkus:quarkus-maven-plugin:create \
       -DprojectGroupId=br.com.fiap \
       -DprojectArtifactId=eco-park \
       -DclassName="br.com.fiap.infrastructure.api.rest.EcoParkApplication" \
       -Dpath="/api"
   ```

2. Configure o arquivo `pom.xml` com as dependências necessárias:
    - quarkus-resteasy-reactive-jackson
    - quarkus-jdbc-postgresql
    - quarkus-agroal (para pool de conexões)
    - quarkus-arc (para injeção de dependências)

3. Implemente a estrutura de pacotes seguindo a arquitetura limpa:
   ```
   src/main/java/br/com/fiap/
   └── domain/
       ├── exception/
       ├── model/
       └── repository/
   ```

4. Implemente as exceções de domínio no pacote `br.com.fiap.domain.exception`:
    - `ValidacaoDominioException.java`: para validações de regras de negócio
    - `EntidadeNaoEncontradaException.java`: para quando uma entidade não é encontrada
    - `ClienteConsistenceError.java`: para erros específicos de consistência de cliente

5. Implemente as classes de domínio no pacote `br.com.fiap.domain.model`:

   a. `Endereco.java`:
    - Atributos: cep (String), numero (String), complemento (String)
    - Validações: CEP no formato XXXXX-XXX

   b. `Cliente.java`:
    - Atributos: nome, cpf, telefone, email, endereco, anoNascimento, ativo, versao
    - Validações:
        - Nome: mínimo duas palavras com pelo menos 3 caracteres cada
        - CPF: 11 dígitos, validação de dígitos verificadores
        - Telefone: formato (XX)XXXXX-XXXX
        - Email: formato válido
        - Idade: mínimo 18 anos

   c. `Carro.java`:
    - Atributos: marca, modelo, ano, placa, porte (enum CarroPorte)
    - Enum CarroPorte: PEQUENO, MEDIO, GRANDE
    - Validações: placa no formato padrão

   d. `Contrato.java`:
    - Atributos: id, clienteCpf, carros (Set<Carro>), dataInicio, dataFim, versao
    - Validações:
        - Máximo 2 carros por contrato
        - Data de início não pode ser anterior à data atual para novos contratos
        - Data de fim não pode ser anterior à data de início

### Critérios de Avaliação
- Correta implementação das classes de domínio com seus atributos e métodos
- Implementação adequada das validações de domínio
- Uso correto de encapsulamento e princípios OO
- Organização do código seguindo a arquitetura proposta

### Estrutura de Pacotes Esperada
```
src/main/java/br/com/fiap/
└── domain/
    ├── exception/
    │   ├── EntidadeNaoEncontradaException.java
    │   └── ValidacaoDominioException.java
    └── model/
        ├── Carro.java
        ├── Cliente.java
        ├── Contrato.java
        └── Endereco.java
```

## Atividade 2: Implementação dos Repositories com JDBC

### Objetivo
Implementar a camada de repositórios utilizando JDBC para persistência de dados, seguindo os contratos definidos na camada de domínio.

### Estrutura do banco de dados

Queries para criação da estrutura do banco de dados:

```
create table CLIENTE
(
    CPF            VARCHAR2(11)  not null
        constraint CPF_PK
            primary key,
    NOME           VARCHAR2(300) not null,
    EMAIL          VARCHAR2(256),
    ANO_NASCIMENTO NUMBER,
    CEP            VARCHAR2(9)   not null,
    NUMERO         VARCHAR2(64),
    COMPLEMENTO    VARCHAR2(256),
    TELEFONE       VARCHAR2(14),
    LAST_UPDATE    TIMESTAMP(6),
    CREATED_AT     TIMESTAMP(6),
    ATIVO          NUMBER(1)     not null,
    VERSION        NUMBER        not null
);
create table CARRO
(
    ID               NUMBER generated as identity
        constraint CARRO_PK
            primary key,
    MARCA            VARCHAR2(128),
    MODELO           VARCHAR2(128),
    ANO              NUMBER,
    CREATED_AT       TIMESTAMP(6),
    LAST_UPDATED     TIMESTAMP(6),
    PLACA            VARCHAR2(7) not null,
    PORTE            VARCHAR2(7) not null,
    CONTRATO_ID      NUMBER      not null,
    CONTRATO_VERSION NUMBER      not null
)
;
create table CONTRATO
(
    ID          NUMBER generated as identity,
    CPF         VARCHAR2(11) not null
        primary key
        constraint CONTRATO_CLIENTE__FK
            references PF1910.CLIENTE,
    DATA_INICIO TIMESTAMP(6),
    DATA_FIM    TIMESTAMP(6),
    LAST_UPDATE TIMESTAMP(6),
    CREATED_AT  TIMESTAMP(6),
    VERSION     NUMBER
);
```

### Instruções Detalhadas
1. Defina as interfaces de repositório na camada de domínio:

   a. `ClienteRepository.java`:
   ```java
   public interface ClienteRepository {
       Cliente salvar(Cliente cliente);
       Cliente buscarPorCpf(String cpf) throws EntidadeNaoEncontradaException;
       Cliente editar(Cliente cliente);
       List<Cliente> buscarTodos();
       void desativar(String cpf, Long versao);
       void reativar(String cpf, Long versao) throws EntidadeNaoEncontradaException;
   }
   ```

   b. `ContratoRepository.java`:
   ```java
   public interface ContratoRepository {
       Contrato salvar(Contrato contrato);
       Contrato buscarPorId(Long id) throws EntidadeNaoEncontradaException;
       Contrato editar(Contrato contrato);
       List<Contrato> buscarPorClienteCpf(String cpf);
       Contrato buscarAtivoPorClienteCpf(String cpf);
       void finalizar(Long id, Long version);
   }
   ```

2. Implemente a infraestrutura de conexão com o banco de dados:

   a. `DatabaseConnection.java`:
   ```java
   public interface DatabaseConnection {
       Connection getConnection() throws SQLException;
   }
   ```

   b. `DatabaseConnectionImpl.java`:
   ```java
   public class DatabaseConnectionImpl implements DatabaseConnection {
       DataSource dataSource;
       
       @Override
       public Connection getConnection() throws SQLException {
           return dataSource.getConnection();
       }
   }
   ```

3. Configure o arquivo `application.properties` para conexão com o banco de dados:
   ```properties
quarkus.datasource.db-kind=oracle
quarkus.datasource.jdbc.driver=oracle.jdbc.driver.OracleDriver
quarkus.datasource.jdbc.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
quarkus.datasource.username=${DB_FIAP_USERNAME}
quarkus.datasource.password=${DB_FIAP_PASSWORD}
   ```

4. Implemente os scripts SQL para criação das tabelas:
    - `clientes`: para armazenar os dados dos clientes
    - `carros`: para armazenar os dados dos carros
    - `contratos`: para armazenar os contratos
    - `contrato_carros`: tabela de relacionamento entre contratos e carros

5. Implemente as classes concretas de repositório:

   a. `JdbcClienteRepository.java`:
    - Implementar todos os métodos da interface ClienteRepository
    - Utilizar prepared statements para evitar SQL injection
    - Implementar mapeamento entre objetos de domínio e registros de banco de dados
    - Tratar adequadamente as exceções de banco de dados

   b. `JdbcContratoRepository.java`:
    - Implementar todos os métodos da interface ContratoRepository
    - Gerenciar transações para operações que envolvem múltiplas tabelas
    - Implementar mapeamento entre objetos de domínio e registros de banco de dados
    - Tratar adequadamente as exceções de banco de dados

### Critérios de Avaliação
- Correta implementação das interfaces de repositório
- Implementação adequada das classes concretas com JDBC
- Tratamento adequado de exceções
- Mapeamento correto entre objetos de domínio e registros de banco de dados
- Uso adequado de prepared statements para evitar SQL injection

### Estrutura de Pacotes Esperada
```
src/main/java/br/com/fiap/
├── domain/
│   ├── exception/
│   │   └── EntidadeNaoEncontradaException.java
│   ├── model/
│   │   ├── Carro.java
│   │   ├── Cliente.java
│   │   ├── Contrato.java
│   │   └── Endereco.java
│   └── repository/
│       ├── ClienteRepository.java
│       └── ContratoRepository.java
└── infrastructure/
    ├── exception/
    │   └── InfrastructureException.java
    └── persistence/
        ├── DatabaseConnection.java
        ├── JdbcDatabaseConnection.java
        └── repository/
            ├── JdbcClienteRepository.java
            └── JdbcContratoRepository.java
```