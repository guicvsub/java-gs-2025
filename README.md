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
- [Padrões e Boas Práticas](#padrões-e-boas-práticas)

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL 8.0+**
- **Maven**
- **Lombok**
- **Bean Validation**

## 🏗️ Arquitetura

O projeto segue os princípios de **Arquitetura Orientada a Serviços (SOA)** e **Clean Code**, com separação clara de responsabilidades:

### Camadas da Aplicação

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← Recebe requisições HTTP
├─────────────────────────────────────┤
│         Service Layer               │  ← Lógica de negócio (SOA)
├─────────────────────────────────────┤
│         Repository Layer            │  ← Acesso a dados
├─────────────────────────────────────┤
│         Model Layer                 │  ← Entidades JPA
└─────────────────────────────────────┘
```

### Estrutura de Diretórios

```
src/main/java/br/com/cashplus/
├── controller/          # Controladores REST
├── service/             # Serviços de negócio (SOA)
├── repository/          # Repositórios JPA
├── model/               # Entidades do banco
├── dto/                 # Data Transfer Objects
├── exception/           # Exceções personalizadas
├── validation/          # Validadores customizados
├── config/              # Configurações (Sessão, Interceptors)
└── util/                # Utilitários
```

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MySQL 8.0+

## 🔧 Instalação

### 1. Instalar MySQL no Ubuntu

```bash
# Atualizar repositórios
sudo apt update

# Instalar MySQL Server
sudo apt install mysql-server mysql-workbench

# Iniciar MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# Verificar status
sudo systemctl status mysql
```

### 2. Configurar MySQL

```bash
# Acessar MySQL como root
sudo mysql

# Criar banco de dados
CREATE DATABASE cashplus;

# Criar usuário (opcional, se não usar root)
CREATE USER 'cashplus_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON cashplus.* TO 'cashplus_user'@'localhost';
FLUSH PRIVILEGES;

# Sair do MySQL
EXIT;
```

### 3. Configurar Aplicação

Edite o arquivo `src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cashplus
spring.datasource.username=root
spring.datasource.password=root
```

## 🚀 Executando a Aplicação

### Opção 1: Maven

```bash
# Compilar e executar
mvn spring-boot:run
```

### Opção 2: Executar JAR

```bash
# Compilar
mvn clean package

# Executar
java -jar target/CashPlusAssist-API-1.0.0.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## 📁 Estrutura do Projeto

```
CashPlusAssist-API/
├── src/
│   ├── main/
│   │   ├── java/br/com/cashplus/
│   │   │   ├── controller/
│   │   │   │   ├── OperadorController.java
│   │   │   │   ├── TransacaoController.java
│   │   │   │   └── SessionController.java
│   │   │   ├── service/
│   │   │   │   ├── OperadorService.java
│   │   │   │   └── TransacaoService.java
│   │   │   ├── repository/
│   │   │   │   ├── OperadorRepository.java
│   │   │   │   └── TransacaoRepository.java
│   │   │   ├── model/
│   │   │   │   ├── Operador.java
│   │   │   │   └── Transacao.java
│   │   │   ├── dto/
│   │   │   │   ├── OperadorDTO.java
│   │   │   │   ├── TransacaoDTO.java
│   │   │   │   └── ErrorResponseDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ValidationException.java
│   │   │   │   └── SessionException.java
│   │   │   ├── validation/
│   │   │   │   ├── CPF.java
│   │   │   │   ├── CPFValidator.java
│   │   │   │   ├── EnumValue.java
│   │   │   │   └── EnumValueValidator.java
│   │   │   ├── config/
│   │   │   │   ├── SessionConfig.java
│   │   │   │   └── SessionInterceptor.java
│   │   │   ├── util/
│   │   │   │   └── SessionManager.java
│   │   │   └── CashPlusAssistApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── messages.properties
│   └── test/
├── pom.xml
└── README.md
```

## 🌐 Endpoints da API

### 🔐 Sessão

#### Criar Sessão
```http
POST /sessao/criar?userId=user123
```

**Resposta:**
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
POST /operadores
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}

Body:
{
  "nome": "João Silva",
  "cpf": "12345678909",
  "turno": "MANHA"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678909",
  "turno": "MANHA"
}
```

#### Listar Operadores
```http
GET /operadores
Headers: X-Session-Token: {token}
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
  }
]
```

#### Buscar Operador por ID
```http
GET /operadores/{id}
Headers: X-Session-Token: {token}
```

#### Atualizar Operador
```http
PUT /operadores/{id}
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}

Body:
{
  "nome": "João Silva Santos",
  "cpf": "12345678909",
  "turno": "TARDE"
}
```

#### Deletar Operador
```http
DELETE /operadores/{id}
Headers: X-Session-Token: {token}
```

**Resposta (204 No Content)**

---

### 💰 Transações

#### Criar Transação
```http
POST /transacoes
Headers: 
  Content-Type: application/json
  X-Session-Token: {token}

Body:
{
  "valor": 150.50,
  "tipoPagamento": "CARTAO"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "valor": 150.50,
  "tipoPagamento": "CARTAO",
  "riscoFraude": "MEDIO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

> **Nota:** O campo `riscoFraude` é calculado automaticamente pelo serviço:
> - **DINHEIRO** ou **PIX**: sempre `BAIXO`
> - **CARTAO**: 
>   - `BAIXO` se valor < R$ 100
>   - `MEDIO` se valor entre R$ 100 e R$ 500
>   - `ALTO` se valor > R$ 500

#### Listar Transações
```http
GET /transacoes
Headers: X-Session-Token: {token}
```

#### Buscar Transação por ID
```http
GET /transacoes/{id}
Headers: X-Session-Token: {token}
```

#### Deletar Transação
```http
DELETE /transacoes/{id}
Headers: X-Session-Token: {token}
```

---

## 🔒 Sistema de Sessão

A API utiliza um sistema de sessão baseado em tokens para autenticação:

1. **Criar Sessão**: Chame `/sessao/criar` para obter um token
2. **Usar Token**: Inclua o header `X-Session-Token` em todas as requisições (exceto criação de sessão)
3. **Validade**: Tokens expiram após 30 minutos de inatividade

### Exemplo de Uso com cURL

```bash
# 1. Criar sessão
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=user123" | jq -r '.token')

# 2. Usar token em requisições
curl -X GET "http://localhost:8080/operadores" \
  -H "X-Session-Token: $TOKEN"
```

## ✅ Validações

### Validações de Operador

- **nome**: Obrigatório, mínimo 3 caracteres
- **cpf**: Obrigatório, formato válido (validação de dígitos verificadores)
- **turno**: Obrigatório, valores aceitos: `MANHA`, `TARDE`, `NOITE`

### Validações de Transação

- **valor**: Obrigatório, deve ser positivo
- **tipoPagamento**: Obrigatório, valores aceitos: `DINHEIRO`, `CARTAO`, `PIX`

### Validadores Customizados

- **@CPF**: Valida CPF com algoritmo de dígitos verificadores
- **@EnumValue**: Valida se o valor pertence a um enum específico

## 🚨 Tratamento de Erros

A API retorna erros no seguinte formato:

```json
{
  "timestamp": "2025-01-14T16:40:32",
  "status": 400,
  "error": "Validation Error",
  "messages": [
    "cpf: CPF inválido",
    "turno: Valor inválido. Valores aceitos: MANHA / TARDE / NOITE"
  ],
  "path": "/operadores"
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

## 📐 Padrões e Boas Práticas

### Arquitetura

- ✅ **MVC (Model-View-Controller)**: Separação clara de responsabilidades
- ✅ **SOA (Service-Oriented Architecture)**: Serviços independentes e reutilizáveis
- ✅ **RESTful**: Uso adequado de métodos HTTP (GET, POST, PUT, DELETE)
- ✅ **Clean Code**: Código legível, manutenível e testável

### Segurança

- ✅ **Validação de Entrada**: Todas as entradas são validadas
- ✅ **Sistema de Sessão**: Tokens para autenticação
- ✅ **Prevenção de Injeção**: Uso de JPA/Hibernate (prepared statements)

### Tratamento de Dados

- ✅ **DTOs**: Separação entre modelos de domínio e DTOs de API
- ✅ **Validações Bean Validation**: Validações declarativas
- ✅ **Exceções Personalizadas**: Tratamento centralizado de erros

### Banco de Dados

- ✅ **JPA/Hibernate**: ORM para acesso a dados
- ✅ **Migrations Automáticas**: `spring.jpa.hibernate.ddl-auto=update`
- ✅ **Transações**: Uso de `@Transactional` nos serviços

## 📝 Exemplos de Requisições

### Criar Operador Completo

```bash
# 1. Criar sessão
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar" | jq -r '.token')

# 2. Criar operador
curl -X POST "http://localhost:8080/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "turno": "TARDE"
  }'
```

### Criar Transação com Cálculo Automático de Risco

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 250.00,
    "tipoPagamento": "CARTAO"
  }'
```

## 🔍 Logs

A aplicação gera logs detalhados:

- **SQL Queries**: Todas as queries são logadas (modo DEBUG)
- **Requisições HTTP**: Logs de requisições e respostas
- **Erros**: Stack traces completos para debugging

## 📚 Dependências Principais

- `spring-boot-starter-web`: Framework web RESTful
- `spring-boot-starter-validation`: Validações Bean Validation
- `spring-boot-starter-data-jpa`: Persistência JPA/Hibernate
- `mysql-connector-j`: Driver MySQL
- `lombok`: Redução de boilerplate
- `spring-boot-starter-aop`: Suporte a AOP

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é um exemplo educacional.

## 👨‍💻 Autor

CashPlusAssist - Assistente de Atendimento Inteligente para Operadores de Caixa

---

**Tecnologia que empodera o profissional, não o substitui.** 🚀

