# 🎫 EventHub API

API RESTful para gestão de eventos e venda de ingressos, desenvolvida com foco em **concorrência segura**, **boas práticas (SOLID)** e princípios do **12-Factor App**.

---

## 🚀 Tecnologias Utilizadas

*   **Java 17** (LTS)
*   **Spring Boot 3.5+**
*   **Spring Data JPA** (Hibernate)
*   **H2 Database** (Desenvolvimento/Testes em memória)
*   **PostgreSQL Driver** (Pronto para Produção)
*   **Bean Validation** (Hibernate Validator)
*   **JUnit 5 & Mockito** (Testes Unitários)
*   **OpenAPI / Swagger** (Documentação Viva)
*   **Logback / Logstash Encoder** (Logs Estruturados JSON)

---

## 🏃‍♂️ Como Executar

### Pré-requisitos
*   Java 17+ instalado.
*   Maven (Opcional, pois o projeto inclui o wrapper `mvnw`).

### Executando Localmente (H2 em Memória)
O projeto vem configurado por padrão para usar o banco H2 em memória, não exigindo instalação externa de banco de dados.

1.  Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/eventhub-api.git
    cd eventhub-api
    ```

2.  Execute a aplicação:
    *   **Linux/Mac**: `./mvnw spring-boot:run`
    *   **Windows**: `.\mvnw.cmd spring-boot:run`

3.  Acesse a documentação da API (Swagger UI):
    *   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧠 Decisões Técnicas e Arquitetura

### 1. Tratamento de Concorrência (Venda de Ingressos)
A principal complexidade do teste era garantir que não vendêssemos mais ingressos do que a capacidade permite.

*   **Solução Adotada**: **Atomicidade no Banco de Dados**.
*   **Por quê?**: Validar a capacidade na aplicação Java (`if (evento.getCapacidade() > 0)`) cria uma **Race Condition** em alta concorrência. Duas threads poderiam ler a capacidade ao mesmo tempo e vender duplicado.
*   **Implementação**: Utilizei uma query customizada no repositório (`UPDATE evento SET capacidade = capacidade - 1 WHERE id = ? AND capacidade > 0`). O banco de dados garante o lock da linha durante a atualização, tornando a operação atômica e segura.

### 2. Padrão 12-Factor App
O projeto segue princípios modernos de Cloud Native:
*   **Configurações (Fator III)**: Todas as credenciais e conexões de banco de dados são externalizadas via variáveis de ambiente (`SPRING_DATASOURCE_URL`, etc) no `application.properties`. Não há senhas hardcoded.
*   **Logs (Fator XI)**: A aplicação envia logs estruturados em JSON para a `stdout`, facilitando a ingestão por ferramentas como ELK Stack ou Datadog.
*   **Descartabilidade (Fator IX)**: Implementação de `Graceful Shutdown` para garantir que processos em andamento terminem antes da aplicação parar.

### 3. Organização de Código (Clean Code)
*   **DTO (Data Transfer Object)**: Entidades (`@Entity`) nunca são expostas diretamente na API. DTOs (`Request`/`Response`) desacoplam o domínio da apresentação e protegem dados internos.
*   **Validações**: Uso extensivo de Bean Validation (`@NotBlank`, `@Future`) e validadores customizados (`@DataNaoPassada`) para garantir integridade logo na entrada do Controller (Fail-fast).
*   **Exceções de Negócio**: Não retornamos exceções genéricas. Erros de negócio (`EventoLotadoException`) são capturados por um `GlobalExceptionHandler` que devolve respostas HTTP padronizadas e claras.

---

## 🧪 Testes

Os testes unitários focam na regra de negócio mais crítica: a compra de ingressos.

Para rodar os testes:
```bash
./mvnw test
```

A suíte cobre:
*   ✅ Compra com sucesso (decremento de capacidade).
*   ✅ Tentativa de compra em evento lotado.
*   ✅ Tentativa de compra em evento inexistente.
*   ✅ Listagem de histórico de participante.

---

## 📚 Endpoints Principais

A coleção completa do Postman está disponível no arquivo `EventHub-API.postman_collection.json` na raiz do projeto.

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/eventos` | Cria um novo evento |
| `GET` | `/api/eventos` | Lista todos eventos |
| `POST` | `/api/participantes` | Cadastra um participante |
| `POST` | `/api/ingressos/comprar` | Realiza a compra (com validação de estoque) |
| `GET` | `/api/ingressos/participante/{id}` | Histórico de compras |


