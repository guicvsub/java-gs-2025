# CashPlusAssist-API

**Assistente de Atendimento Inteligente para Operadores de Caixa**

Sistema inteligente que auxilia atendentes de caixa a trabalharem com mais rapidez, precisão e segurança, reduzindo erros de troco, detectando possíveis fraudes e acelerando o atendimento.

## 📋 Índice

- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Executando a Aplicação](#executando-a-aplicação)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Sistema de Sessão](#sistema-de-sessão)
- [Validações](#validações)
- [Tratamento de Erros](#tratamento-de-erros)
- [Testes de Carga](#testes-de-carga)
- [Padrões e Boas Práticas](#padrões-e-boas-práticas)

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
- **JUnit 5** (Testes)

## 🏗️ Arquitetura

O projeto segue os princípios de **Arquitetura Limpa** e **Domain-Driven Design (DDD)**, com separação clara de responsabilidades:

### Camadas da Aplicação

```
┌─────────────────────────────────────┐
│      Controller Layer (REST)        │  ← Interface HTTP
├─────────────────────────────────────┤
│      Service Layer (Aplicação)      │  ← Lógica de aplicação
├─────────────────────────────────────┤
│      Repository Layer               │  ← Persistência
├─────────────────────────────────────┤
│      Model Layer (Domínio)          │  ← Entities, VOs, Enums
│      - Entities                     │
│      - Value Objects                │
│      - Enums                        │
└─────────────────────────────────────┘
```

### Estrutura de Diretórios

```
src/main/java/br/com/cashplus/
├── controller/              # Controllers REST
├── service/                 # Serviços de aplicação
│   └── RiscoFraudeService   # Consumo de API externa
├── repository/              # Repositórios JPA
├── model/                   # Camada de domínio
│   ├── enums/              # Enums (TurnoEnum, TipoPagamentoEnum, RiscoFraudeEnum)
│   └── valueobject/        # Value Objects (CPF)
├── dto/                     # Data Transfer Objects
│   ├── request/            # DTOs de entrada
│   ├── response/           # DTOs de saída
│   └── external/           # DTOs para APIs externas
├── exception/               # Exceções customizadas
├── validation/              # Validadores customizados
├── config/                  # Configurações
└── util/                    # Utilitários
```

## 📦 Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git** (opcional)

## 🔧 Instalação

### 1. Clonar o Repositório

```bash
git clone <repository-url>
cd CashPlusAssist-API/java-gs-2025
```

### 2. Configurar MySQL

```bash
# Acessar MySQL
sudo mysql -u root -p

# Criar banco de dados
CREATE DATABASE cashplus CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Criar usuário (opcional)
CREATE USER 'cashplus_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON cashplus.* TO 'cashplus_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Configurar Aplicação

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/cashplus
spring.datasource.username=root
spring.datasource.password=root
```

## 🚀 Executando a Aplicação

### Opção 1: Maven (Recomendado)

```bash
# Clean e rebuild (sem testes)
mvn clean install -DskipTests

# Executar aplicação
mvn spring-boot:run
```

### Opção 2: Executar JAR

```bash
# Compilar
mvn clean package -DskipTests

# Executar
java -jar target/CashPlusAssist-API-1.0.0.jar
```

### Opção 3: IDE

Execute a classe `CashPlusAssistApiApplication.java` diretamente na sua IDE.

A aplicação estará disponível em: **`http://localhost:8080`**

## 📁 Estrutura do Projeto

```
CashPlusAssist-API/
├── src/
│   ├── main/
│   │   ├── java/br/com/cashplus/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── OperadorController.java
│   │   │   │   ├── TransacaoController.java
│   │   │   │   └── SessionController.java
│   │   │   ├── service/             # Lógica de aplicação
│   │   │   │   ├── OperadorService.java
│   │   │   │   ├── TransacaoService.java
│   │   │   │   └── RiscoFraudeService.java
│   │   │   ├── repository/          # Persistência
│   │   │   │   ├── OperadorRepository.java
│   │   │   │   └── TransacaoRepository.java
│   │   │   ├── model/               # Domínio
│   │   │   │   ├── enums/
│   │   │   │   │   ├── TurnoEnum.java
│   │   │   │   │   ├── TipoPagamentoEnum.java
│   │   │   │   │   └── RiscoFraudeEnum.java
│   │   │   │   ├── valueobject/
│   │   │   │   │   └── CPF.java
│   │   │   │   ├── Operador.java
│   │   │   │   └── Transacao.java
│   │   │   ├── dto/                 # DTOs
│   │   │   │   ├── request/
│   │   │   │   │   ├── OperadorRequestDTO.java
│   │   │   │   │   └── TransacaoRequestDTO.java
│   │   │   │   ├── response/
│   │   │   │   │   ├── OperadorResponseDTO.java
│   │   │   │   │   └── TransacaoResponseDTO.java
│   │   │   │   ├── external/
│   │   │   │   │   ├── RiscoFraudeRequestDTO.java
│   │   │   │   │   └── RiscoFraudeResponseDTO.java
│   │   │   │   └── ErrorResponseDTO.java
│   │   │   ├── exception/           # Exceções
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ValidationException.java
│   │   │   │   └── SessionException.java
│   │   │   ├── validation/          # Validadores
│   │   │   │   ├── CPF.java
│   │   │   │   ├── CPFValidator.java
│   │   │   │   ├── EnumValue.java
│   │   │   │   └── EnumValueValidator.java
│   │   │   ├── config/              # Configurações
│   │   │   │   ├── SessionConfig.java
│   │   │   │   ├── SessionInterceptor.java
│   │   │   │   └── WebClientConfig.java
│   │   │   ├── util/
│   │   │   │   └── SessionManager.java
│   │   │   └── CashPlusAssistApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── messages.properties
│   │       └── db/migration/        # Migrações Flyway
│   │           ├── V1__Initial_Schema.sql
│   │           └── V2__Add_Operador_Id_To_Transacoes.sql
│   └── test/
│       └── java/                    # Testes unitários
├── scripts-teste-carga/             # Scripts de teste de carga
│   ├── jmeter/
│   ├── k6/
│   └── README.md
├── pom.xml
├── README.md
└── REFATORACAO.md
```

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

**Resposta (200 OK):**
```json
{
  "status": "valid",
  "message": "Sessão válida"
}
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
  "cpf": "123.456.789-09",
  "turno": "MANHA"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-09",
  "turno": "MANHA",
  "turnoDescricao": "Manhã"
}
```

#### Listar Operadores
```http
GET /api/operadores
Headers: X-Session-Token: {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "123.456.789-09",
    "turno": "MANHA",
    "turnoDescricao": "Manhã"
  }
]
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

Body:
{
  "nome": "João Silva Santos",
  "cpf": "123.456.789-09",
  "turno": "TARDE"
}
```

#### Deletar Operador
```http
DELETE /api/operadores/{id}
Headers: X-Session-Token: {token}
```

**Resposta (204 No Content)**

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

> **Nota:** O campo `riscoFraude` é calculado automaticamente:
> - **DINHEIRO** ou **PIX**: sempre `BAIXO`
> - **CARTAO**: 
>   - `BAIXO` se valor < R$ 100
>   - `MEDIO` se valor entre R$ 100 e R$ 500
>   - `ALTO` se valor > R$ 500

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

**Resposta (204 No Content)**

---

## 🔒 Sistema de Sessão

A API utiliza um sistema de sessão baseado em tokens:

1. **Criar Sessão**: Chame `/sessao/criar` para obter um token
2. **Usar Token**: Inclua o header `X-Session-Token` em todas as requisições (exceto criação de sessão)
3. **Validade**: Tokens expiram após 30 minutos de inatividade

### Exemplo de Uso com cURL

```bash
# 1. Criar sessão
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=user123" | jq -r '.token')

# 2. Usar token em requisições
curl -X GET "http://localhost:8080/api/operadores" \
  -H "X-Session-Token: $TOKEN"
```

## ✅ Validações

### Validações de Operador

- **nome**: Obrigatório, mínimo 3 caracteres, máximo 100 caracteres
- **cpf**: Obrigatório, formato válido (validação de dígitos verificadores)
- **turno**: Obrigatório, valores aceitos: `MANHA`, `TARDE`, `NOITE`

### Validações de Transação

- **valor**: Obrigatório, deve ser positivo (> 0)
- **tipoPagamento**: Obrigatório, valores aceitos: `DINHEIRO`, `CARTAO`, `PIX`
- **operadorId**: Opcional (Long positivo)

### Validadores Customizados

- **@CPF**: Valida CPF com algoritmo de dígitos verificadores
- **@EnumValue**: Valida se o valor pertence a um enum específico

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

## 🧪 Testes de Carga

O projeto inclui scripts para testes de carga e performance:

- **JMeter**: `scripts-teste-carga/jmeter/load-test.jmx`
- **k6**: `scripts-teste-carga/k6/load-test.js`

Para mais informações, consulte: [scripts-teste-carga/README.md](scripts-teste-carga/README.md)

## 📐 Padrões e Boas Práticas Implementadas

### Arquitetura

- ✅ **Separação de Camadas**: Controller → Service → Repository → Model
- ✅ **DDD**: Entities, Value Objects, Enums
- ✅ **RESTful**: Uso adequado de métodos HTTP
- ✅ **Clean Code**: Código legível e manutenível

### Domain-Driven Design

- ✅ **Entities**: `Operador`, `Transacao` com identidade própria
- ✅ **Value Objects**: `CPF` (imutável, encapsula validação)
- ✅ **Enums**: `TurnoEnum`, `TipoPagamentoEnum`, `RiscoFraudeEnum`
- ✅ **Repositories**: Abstração de persistência

### Segurança e Validação

- ✅ **Validação de Entrada**: Todas as entradas são validadas
- ✅ **Sistema de Sessão**: Tokens para autenticação
- ✅ **Prevenção de Injeção**: Uso de JPA/Hibernate
- ✅ **DTOs Separados**: Request e Response DTOs

### Integração com APIs Externas

- ✅ **WebClient**: Consumo de APIs REST
- ✅ **Timeout Configurável**: 5 segundos (padrão)
- ✅ **Retry Automático**: 2 tentativas com delay
- ✅ **Fallback**: Cálculo local em caso de falha
- ✅ **DTOs Externos**: `RiscoFraudeRequestDTO`, `RiscoFraudeResponseDTO`

### Banco de Dados

- ✅ **Flyway**: Versionamento de migrações
- ✅ **JPA/Hibernate**: ORM para acesso a dados
- ✅ **Transações**: Uso de `@Transactional`
- ✅ **Relacionamentos**: `Transacao` ↔ `Operador` (ManyToOne)

## 📝 Exemplos de Requisições

### Fluxo Completo: Criar Operador e Transação

```bash
# 1. Criar sessão
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=operador1" | jq -r '.token')

# 2. Criar operador
curl -X POST "http://localhost:8080/api/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "987.654.321-00",
    "turno": "TARDE"
  }'

# 3. Criar transação associada ao operador
curl -X POST "http://localhost:8080/api/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 250.00,
    "tipoPagamento": "CARTAO",
    "operadorId": 1
  }'
```

## 🔍 Logs

A aplicação gera logs detalhados:

- **SQL Queries**: Todas as queries são logadas (modo DEBUG)
- **Requisições HTTP**: Logs de requisições e respostas
- **Erros**: Stack traces completos para debugging
- **Flyway**: Logs de migrações aplicadas

## 📚 Dependências Principais

- `spring-boot-starter-web`: Framework web RESTful
- `spring-boot-starter-webflux`: WebClient para APIs externas
- `spring-boot-starter-validation`: Validações Bean Validation
- `spring-boot-starter-data-jpa`: Persistência JPA/Hibernate
- `flyway-core` / `flyway-mysql`: Versionamento de banco
- `mysql-connector-j`: Driver MySQL
- `lombok`: Redução de boilerplate
- `spring-boot-starter-aop`: Suporte a AOP

## 🎯 Funcionalidades Principais

1. **Gerenciamento de Operadores**
   - CRUD completo
   - Validação de CPF
   - Gestão de turnos

2. **Gerenciamento de Transações**
   - Criação de transações
   - Cálculo automático de risco de fraude
   - Associação com operadores

3. **Sistema de Sessão**
   - Autenticação via tokens
   - Validação de sessão

4. **API Externa de Risco de Fraude**
   - Integração configurável
   - Fallback automático
   - Tratamento de erros e timeouts

## 📖 Documentação Adicional

- [REFATORACAO.md](REFATORACAO.md) - Detalhes das refatorações implementadas
- [scripts-teste-carga/README.md](scripts-teste-carga/README.md) - Guia de testes de carga

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é um exemplo educacional.

## 👥 Equipe de Desenvolvimento

- **Gabriel Souza Fiore** – RM553710
- **Guilherme Santiago** – RM552321
- **Gustavo Gouvêa Soares** – RM553842

## 👨‍💻 Autor

CashPlusAssist - Assistente de Atendimento Inteligente para Operadores de Caixa

---

**Tecnologia que empodera o profissional, não o substitui.** 🚀
