# API de Transações

## Descrição

API para armazenamento e cálculo de estatísticas de transações financeiras.

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Maven
- Docker
- Spring Actuator
- Swagger

## Funcionalidades

- **Salvar Transação**: Endpoint para salvar uma nova transação.
- **Deletar Transações**: Endpoint para deletar todas as transações.
- **Obter Estatísticas**: Endpoint para obter estatísticas das transações realizadas em um intervalo de tempo.

## Endpoints

### Salvar Transação

- **URL**: `/transacao`
- **Método**: `POST`
- **Request Body**:
  ```json
  {
    "value": 100.0,
    "dateHour": "2023-10-10T10:00:00Z"
  }
- **Response**: `201 Created`

### Deletar Transações

- **URL**: `/transacao`
- **Método**: `DELETE`
- **Response**: `200 OK`

### Obter Estatísticas

- **URL**: `/estatistica`
- **Método**: `GET`
- **Query Params**: `intervalo (opcional, padrão: 60 segundos)`
- **Response**:
  ```json
  {
    "count": 0,
    "sum": 0,
    "avg": 0,
    "min": 0,
    "max": 0
  }

## Como Executar

### Pré-requsisitos

- **Java 21**
- **Maven**
- **Docker**

## Passos

### Clonar o repositório:
  ```sh
  git clone https://github.com/WallisonLucas13/Desafio_Itau_Dev_Jr.git cd Desafio_Itau_Dev_Jr
  ```

### Construir o projeto
  ```sh
  mvn clean package
  ```

### Executar a aplicação:
  ```sh
  java -jar target/transacao-api-0.0.1-SNAPSHOT.jar
  ```

### Executar com Docker:
  ```sh
  docker build -t transacao-api .
  docker run -p 8080:8080 transacao-api
  ```

### Testes
  ```sh
  mvn test
  ```

## Configurações
As configurações da aplicação estão no arquivo:
  ```css
  src/main/resources/application.properties
  ```

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature:
   ```sh
   git checkout -b feature/nova-feature
   ```
3. Commit suas mudanças:
   ```sh
   git commit -m "Adiciona nova feature"
   ```
4. Faça o push para a branch:
   ```sh
   git push origin feature/nova-feature
   ```
5. Abra um Pull Request.
