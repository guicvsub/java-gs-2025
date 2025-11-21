* Aviso: o nome mudou de "CashPlus Assist" para "Scanner Real"

# CashPlusAssist-API

**Assistente de Atendimento Inteligente para Operadores de Caixa**

Sistema que auxilia atendentes de caixa com gestão de operadores e transações, incluindo detecção de risco de fraude através de integração com APIs externas.

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring WebFlux** (WebClient para APIs externas)
- **MySQL 8.0+**
- **Flyway** (Migrações de banco de dados)
- **Maven**
- **Lombok**
- **Bean Validation (Jakarta)**



## 🏗️ Arquitetura

O projeto segue **Arquitetura Limpa** e **Domain-Driven Design (DDD)**, com separação clara de responsabilidades:

```
Controller → Service → Repository → Model
```

### Estrutura de Diretórios

```
src/main/java/br/com/cashplus/
├── controller/              # Controllers REST
├── service/                 # Serviços de aplicação
│   └── RiscoFraudeService   # Consumo de API externa com WebClient
├── repository/              # Repositórios JPA
├── model/                   # Camada de domínio
│   ├── enums/              # Enums (TurnoEnum, TipoPagamentoEnum, RiscoFraudeEnum)
│   └── valueobject/        # Value Objects (CPF)
├── dto/                     # Data Transfer Objects
│   ├── request/            # DTOs de entrada
│   ├── response/           # DTOs de saída
│   └── external/           # DTOs para APIs externas
├── config/                  # Configurações (WebClientConfig)
└── exception/               # Tratamento global de exceções
```

## 📦 Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **MySQL 8.0+**

## 🔧 Instalação e Execução

### 1. Configurar Banco de Dados

Configure o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cashplus
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

O Flyway criará automaticamente as tabelas na primeira execução.

### 2. Executar a Aplicação

```bash
# Clean e rebuild
mvn clean install -DskipTests

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: **`http://localhost:8080`**

## 🌐 Endpoints da API

### Base URL
```
http://localhost:8080
```

### 🔐 Sessão

#### Criar Sessão
```http
POST /sessao/criar?userId=operador123
```

**Resposta (200 OK):**
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Sessão criada com sucesso"
}
```

#### Validar Sessão
```http
POST /sessao/validar
Headers: X-Session-Token: {token}
```

---

### 👤 Operadores

#### Criar Operador
```http
POST /api/operadores
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}

Body:
{
  "nome": "João Silva",
  "cpf": "111.444.777-35",
  "turno": "MANHA"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "111.444.777-35",
  "turno": "MANHA",
  "turnoDescricao": "Manhã"
}
```

#### Listar Operadores
```http
GET /api/operadores
Headers: X-Session-Token: {token}
```

#### Buscar Operador por ID
```http
GET /api/operadores/{id}
Headers: X-Session-Token: {token}
```

#### Atualizar Operador
```http
PUT /api/operadores/{id}
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}
```

#### Deletar Operador
```http
DELETE /api/operadores/{id}
Headers: X-Session-Token: {token}
```

---

### 💰 Transações

#### Criar Transação
```http
POST /api/transacoes
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}

Body:
{
  "valor": 150.50,
  "tipoPagamento": "CARTAO",
  "operadorId": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "valor": 150.50,
  "tipoPagamento": "CARTAO",
  "tipoPagamentoDescricao": "Cartão",
  "riscoFraude": "MEDIO",
  "riscoFraudeDescricao": "Médio",
  "operadorId": 1,
  "operadorNome": "João Silva",
  "dataTransacao": "2025-11-15T09:54:00"
}
```

> **Nota:** O campo `riscoFraude` é calculado automaticamente através de integração com API externa (quando habilitada) ou por regra de negócio local.

#### Listar Transações
```http
GET /api/transacoes
Headers: X-Session-Token: {token}
```

#### Buscar Transação por ID
```http
GET /api/transacoes/{id}
Headers: X-Session-Token: {token}
```

#### Deletar Transação
```http
DELETE /api/transacoes/{id}
Headers: X-Session-Token: {token}
```

---

## 🔒 Sistema de Sessão

A API utiliza um sistema de sessão baseado em tokens:

1. **Criar Sessão**: Chame `/sessao/criar` para obter um token
2. **Usar Token**: Inclua o header `X-Session-Token` em todas as requisições (exceto criação de sessão)
3. **Validade**: Tokens expiram após 30 minutos de inatividade

### Exemplo de Uso

```bash
# 1. Criar sessão
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=user123" | jq -r '.token')

# 2. Usar token em requisições
curl -X GET "http://localhost:8080/api/operadores" \
  -H "X-Session-Token: $TOKEN"
```

---

## 🔌 Integração com API Externa

O projeto implementa integração com APIs externas usando **WebClient** do Spring WebFlux para consumo de serviços REST.

### RiscoFraudeService

O serviço `RiscoFraudeService` consome uma API externa para cálculo de risco de fraude em transações:

**Configuração** (`application.properties`):
```properties
app.risco-fraude.api.url=http://api.riscofraude.com/v1/consulta
app.risco-fraude.api.enabled=true
app.risco-fraude.api.timeout=5
```

**Características da Integração:**

- ✅ **WebClient**: Cliente reativo para consumo de APIs REST
- ✅ **Timeout Configurável**: 5 segundos (padrão)
- ✅ **Retry Automático**: 2 tentativas com delay exponencial
- ✅ **Fallback**: Cálculo local em caso de falha da API externa
- ✅ **DTOs Específicos**: `RiscoFraudeRequestDTO` e `RiscoFraudeResponseDTO` para comunicação externa
- ✅ **Tratamento de Erros**: Timeout, conexão e erros HTTP são tratados adequadamente

**Fluxo de Funcionamento:**

1. Ao criar uma transação, o sistema tenta consultar a API externa de risco de fraude
2. Se a API estiver disponível e responder dentro do timeout, usa o resultado
3. Se houver falha (timeout, erro de conexão, etc.), aplica fallback com cálculo local baseado em regras de negócio
4. O resultado é armazenado na transação

**Exemplo de Requisição Externa:**
```json
POST http://api.riscofraude.com/v1/consulta
{
  "valor": 150.50,
  "tipoPagamento": "CARTAO"
}
```

**Resposta:**
```json
{
  "risco": "MEDIO"
}
```

---

## 🧪 Testes de Carga

O projeto inclui scripts para testes de carga e performance da API.

### Script Bash (Recomendado)

Script simples que funciona sem instalação de ferramentas adicionais:

```bash
# Teste básico (10 usuários, 10 requisições cada)
./scripts-teste-carga/bash/load-test.sh

# Teste com mais carga
CONCURRENT_USERS=20 REQUESTS_PER_USER=20 ./scripts-teste-carga/bash/load-test.sh

# Teste com menos delay
DELAY_BETWEEN_REQUESTS=0.1 ./scripts-teste-carga/bash/load-test.sh
```

**Variáveis de Ambiente:**
- `BASE_URL`: URL da API (padrão: http://localhost:8080)
- `CONCURRENT_USERS`: Número de usuários concorrentes (padrão: 10)
- `REQUESTS_PER_USER`: Requisições por usuário (padrão: 10)
- `DELAY_BETWEEN_REQUESTS`: Delay entre requisições em segundos (padrão: 0.5)

**Exemplo de Resultado:**
```
==========================================
  RESULTADOS DO TESTE
==========================================
Total de Requisições: 100
Requisições Bem-sucedidas: 100 (100.00%)
Requisições com Erro: 0 (0%)

Tempo de Resposta:
  Média: 0.015s
  Mínimo: 0.009s
  Máximo: 0.029s

Throughput: 100.00 requisições/segundo

Distribuição de Códigos HTTP:
  201: 100
==========================================
✅ Teste concluído com sucesso!
```

### Outras Ferramentas

O projeto também inclui scripts para:
- **k6**: `scripts-teste-carga/k6/load-test.js`
- **JMeter**: `scripts-teste-carga/jmeter/load-test.jmx`

Para mais detalhes, consulte: [scripts-teste-carga/README.md](scripts-teste-carga/README.md)

### Métricas Esperadas

- **Taxa de Sucesso**: > 95%
- **Tempo de Resposta Médio**: < 200ms
- **P95 (Percentil 95)**: < 500ms
- **Throughput**: > 50 requisições/segundo
- **Taxa de Erro**: < 5%

---

## ✅ Validações

### Validações de Operador

- **nome**: Obrigatório, mínimo 3 caracteres, máximo 100 caracteres
- **cpf**: Obrigatório, formato válido (validação de dígitos verificadores)
- **turno**: Obrigatório, valores aceitos: `MANHA`, `TARDE`, `NOITE`

### Validações de Transação

- **valor**: Obrigatório, deve ser positivo (> 0)
- **tipoPagamento**: Obrigatório, valores aceitos: `DINHEIRO`, `CARTAO`, `PIX`
- **operadorId**: Opcional (Long positivo)

---

## 🚨 Tratamento de Erros

A API retorna erros no seguinte formato padronizado:

```json
{
  "timestamp": "2025-11-15T09:54:00",
  "status": 400,
  "error": "Validation Error",
  "messages": [
    "cpf: CPF inválido",
    "turno: Valor inválido. Valores aceitos: MANHA / TARDE / NOITE"
  ],
  "path": "/api/operadores"
}
```

### Códigos de Status HTTP

- **200 OK**: Requisição bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Recurso deletado com sucesso
- **400 Bad Request**: Erro de validação ou regra de negócio
- **401 Unauthorized**: Sessão inválida ou expirada
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

---

## 📐 Padrões Implementados

### Arquitetura

- ✅ **Separação de Camadas**: Controller → Service → Repository → Model
- ✅ **DDD**: Entities, Value Objects, Enums
- ✅ **RESTful**: Uso adequado de métodos HTTP
- ✅ **DTOs Separados**: Request e Response DTOs

### Domain-Driven Design

- ✅ **Entities**: `Operador`, `Transacao` com identidade própria
- ✅ **Value Objects**: `CPF` (imutável, encapsula validação)
- ✅ **Enums**: `TurnoEnum`, `TipoPagamentoEnum`, `RiscoFraudeEnum`
- ✅ **Repositories**: Abstração de persistência

### Integração com APIs Externas

- ✅ **WebClient**: Consumo de APIs REST reativo
- ✅ **Timeout Configurável**: 5 segundos (padrão)
- ✅ **Retry Automático**: 2 tentativas com delay
- ✅ **Fallback**: Cálculo local em caso de falha
- ✅ **DTOs Externos**: `RiscoFraudeRequestDTO`, `RiscoFraudeResponseDTO`

### Banco de Dados

- ✅ **Flyway**: Versionamento de migrações
- ✅ **JPA/Hibernate**: ORM para acesso a dados
- ✅ **Transações**: Uso de `@Transactional`
- ✅ **Relacionamentos**: `Transacao` ↔ `Operador` (ManyToOne)

---

## 📖 Documentação Adicional

- [REFATORACAO.md](REFATORACAO.md) - Detalhes das refatorações implementadas
- [scripts-teste-carga/README.md](scripts-teste-carga/README.md) - Guia completo de testes de carga
- [EXEMPLOS_CURL.md](EXEMPLOS_CURL.md) - Exemplos de uso com cURL

---

## 📄 Licença

Este projeto é um exemplo educacional.

## 👥 Equipe de Desenvolvimento

- **Gabriel Souza Fiore** – RM553710
- **Guilherme Santiago** – RM552321
- **Gustavo Gouvêa Soares** – RM553842

---

**CashPlusAssist - Assistente de Atendimento Inteligente para Operadores de Caixa**

