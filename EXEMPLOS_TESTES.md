# Exemplos de Testes - CashPlusAssist API

Este documento contém exemplos de requisições JSON para todos os endpoints da API, incluindo casos de sucesso e erros.

**Base URL:** `http://localhost:8080`

---

## 📋 Índice

1. [Sessão](#sessão)
2. [Operadores](#operadores)
3. [Transações](#transações)

---

## 🔐 Sessão

### POST /sessao/criar

Cria uma nova sessão e retorna um token.

**Query Parameters:**
- `userId` (opcional, default: "user")

#### ✅ Sucesso

**Request:**
```
POST /sessao/criar?userId=operador123
```

**Response (200 OK):**
```json
{
    "token": "abc123def456",
    "message": "Sessão criada com sucesso"
}
```

#### ✅ Sucesso (sem userId - usa default)

**Request:**
```
POST /sessao/criar
```

**Response (200 OK):**
```json
{
    "token": "xyz789abc123",
    "message": "Sessão criada com sucesso"
}
```

---

### POST /sessao/validar

Valida se uma sessão é válida.

**Headers:**
- `X-Session-Token`: Token da sessão

#### ✅ Sessão Válida

**Request:**
```
POST /sessao/validar
Headers: X-Session-Token: abc123def456
```

**Response (200 OK):**
```json
{
    "status": "valid",
    "message": "Sessão válida"
}
```

#### ❌ Sessão Inválida

**Request:**
```
POST /sessao/validar
Headers: X-Session-Token: token-invalido-123
```

**Response (200 OK):**
```json
{
    "status": "invalid",
    "message": "Sessão inválida ou expirada"
}
```

#### ❌ Sem Token

**Request:**
```
POST /sessao/validar
(sem header X-Session-Token)
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Token de sessão é obrigatório",
    "path": "/sessao/validar"
}
```

---

## 👤 Operadores

### POST /operadores

Cria um novo operador.

#### ✅ Sucesso

**Request:**
```json
POST /operadores
Content-Type: application/json

{
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
}
```

#### ❌ CPF Inválido

**Request:**
```json
POST /operadores
Content-Type: application/json

{
    "nome": "Maria Santos",
    "cpf": "12345678900",
    "turno": "TARDE"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "CPF inválido",
    "path": "/operadores"
}
```

#### ❌ Nome Muito Curto

**Request:**
```json
POST /operadores
Content-Type: application/json

{
    "nome": "AB",
    "cpf": "98765432100",
    "turno": "NOITE"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Nome deve ter no mínimo 3 caracteres",
    "path": "/operadores"
}
```

#### ❌ Turno Inválido

**Request:**
```json
POST /operadores
Content-Type: application/json

{
    "nome": "Pedro Costa",
    "cpf": "11122233344",
    "turno": "MADRUGADA"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Turno deve ser MANHA, TARDE ou NOITE",
    "path": "/operadores"
}
```

#### ❌ Campos Obrigatórios Faltando

**Request:**
```json
POST /operadores
Content-Type: application/json

{
    "nome": "Ana"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "CPF é obrigatório",
    "path": "/operadores"
}
```

---

### GET /operadores

Lista todos os operadores.

#### ✅ Sucesso

**Request:**
```
GET /operadores
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "nome": "João Silva",
        "cpf": "12345678909",
        "turno": "MANHA"
    },
    {
        "id": 2,
        "nome": "Maria Santos",
        "cpf": "98765432100",
        "turno": "TARDE"
    }
]
```

---

### GET /operadores/{id}

Busca um operador por ID.

#### ✅ Sucesso

**Request:**
```
GET /operadores/1
```

**Response (200 OK):**
```json
{
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
}
```

#### ❌ Não Encontrado

**Request:**
```
GET /operadores/999
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Operador não encontrado com ID: 999",
    "path": "/operadores/999"
}
```

---

### PUT /operadores/{id}

Atualiza um operador existente.

#### ✅ Sucesso

**Request:**
```json
PUT /operadores/1
Content-Type: application/json

{
    "nome": "João Silva Santos",
    "cpf": "12345678909",
    "turno": "TARDE"
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "nome": "João Silva Santos",
    "cpf": "12345678909",
    "turno": "TARDE"
}
```

#### ❌ Não Encontrado

**Request:**
```json
PUT /operadores/999
Content-Type: application/json

{
    "nome": "Teste",
    "cpf": "11122233344",
    "turno": "MANHA"
}
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Operador não encontrado com ID: 999",
    "path": "/operadores/999"
}
```

---

### DELETE /operadores/{id}

Deleta um operador.

#### ✅ Sucesso

**Request:**
```
DELETE /operadores/1
```

**Response (204 No Content):**
```
(sem corpo)
```

#### ❌ Não Encontrado

**Request:**
```
DELETE /operadores/999
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Operador não encontrado com ID: 999",
    "path": "/operadores/999"
}
```

---

## 💰 Transações

### POST /transacoes

Cria uma nova transação. O risco de fraude é calculado automaticamente:
- **DINHEIRO**: sempre BAIXO
- **PIX**: sempre BAIXO
- **CARTAO**: 
  - BAIXO se valor < 100
  - MEDIO se 100 ≤ valor ≤ 500
  - ALTO se valor > 500

#### ✅ Sucesso - DINHEIRO

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 50.00,
    "tipoPagamento": "DINHEIRO"
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "valor": 50.00,
    "tipoPagamento": "DINHEIRO",
    "riscoFraude": "BAIXO",
    "dataTransacao": "2025-11-14T18:30:00"
}
```

#### ✅ Sucesso - PIX

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 150.50,
    "tipoPagamento": "PIX"
}
```

**Response (201 Created):**
```json
{
    "id": 2,
    "valor": 150.50,
    "tipoPagamento": "PIX",
    "riscoFraude": "BAIXO",
    "dataTransacao": "2025-11-14T18:30:01"
}
```

#### ✅ Sucesso - CARTAO (Risco BAIXO)

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 75.00,
    "tipoPagamento": "CARTAO"
}
```

**Response (201 Created):**
```json
{
    "id": 3,
    "valor": 75.00,
    "tipoPagamento": "CARTAO",
    "riscoFraude": "BAIXO",
    "dataTransacao": "2025-11-14T18:30:02"
}
```

#### ✅ Sucesso - CARTAO (Risco MEDIO)

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 300.00,
    "tipoPagamento": "CARTAO"
}
```

**Response (201 Created):**
```json
{
    "id": 4,
    "valor": 300.00,
    "tipoPagamento": "CARTAO",
    "riscoFraude": "MEDIO",
    "dataTransacao": "2025-11-14T18:30:03"
}
```

#### ✅ Sucesso - CARTAO (Risco ALTO)

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 750.00,
    "tipoPagamento": "CARTAO"
}
```

**Response (201 Created):**
```json
{
    "id": 5,
    "valor": 750.00,
    "tipoPagamento": "CARTAO",
    "riscoFraude": "ALTO",
    "dataTransacao": "2025-11-14T18:30:04"
}
```

#### ❌ Valor Negativo

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": -50.00,
    "tipoPagamento": "DINHEIRO"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Valor deve ser positivo",
    "path": "/transacoes"
}
```

#### ❌ Valor Zero

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 0.00,
    "tipoPagamento": "DINHEIRO"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Valor deve ser positivo",
    "path": "/transacoes"
}
```

#### ❌ Valor Nulo

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "tipoPagamento": "DINHEIRO"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Valor é obrigatório",
    "path": "/transacoes"
}
```

#### ❌ Tipo Pagamento Inválido

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 100.00,
    "tipoPagamento": "BITCOIN"
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Tipo de pagamento deve ser DINHEIRO, CARTAO ou PIX",
    "path": "/transacoes"
}
```

#### ❌ Tipo Pagamento Vazio

**Request:**
```json
POST /transacoes
Content-Type: application/json

{
    "valor": 100.00,
    "tipoPagamento": ""
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Tipo de pagamento é obrigatório",
    "path": "/transacoes"
}
```

---

### GET /transacoes

Lista todas as transações.

#### ✅ Sucesso

**Request:**
```
GET /transacoes
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "valor": 50.00,
        "tipoPagamento": "DINHEIRO",
        "riscoFraude": "BAIXO",
        "dataTransacao": "2025-11-14T18:30:00"
    },
    {
        "id": 2,
        "valor": 75.00,
        "tipoPagamento": "CARTAO",
        "riscoFraude": "BAIXO",
        "dataTransacao": "2025-11-14T18:30:01"
    },
    {
        "id": 3,
        "valor": 300.00,
        "tipoPagamento": "CARTAO",
        "riscoFraude": "MEDIO",
        "dataTransacao": "2025-11-14T18:30:02"
    }
]
```

---

### GET /transacoes/{id}

Busca uma transação por ID.

#### ✅ Sucesso

**Request:**
```
GET /transacoes/1
```

**Response (200 OK):**
```json
{
    "id": 1,
    "valor": 50.00,
    "tipoPagamento": "DINHEIRO",
    "riscoFraude": "BAIXO",
    "dataTransacao": "2025-11-14T18:30:00"
}
```

#### ❌ Não Encontrado

**Request:**
```
GET /transacoes/999
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Transação não encontrada com ID: 999",
    "path": "/transacoes/999"
}
```

---

### DELETE /transacoes/{id}

Deleta uma transação.

#### ✅ Sucesso

**Request:**
```
DELETE /transacoes/1
```

**Response (204 No Content):**
```
(sem corpo)
```

#### ❌ Não Encontrado

**Request:**
```
DELETE /transacoes/999
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2025-11-14T18:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Transação não encontrada com ID: 999",
    "path": "/transacoes/999"
}
```

---

## 📝 Notas Importantes

1. **Valores Monetários**: Use `BigDecimal` no formato JSON (ex: `50.00`, `150.50`)

2. **CPF Válido**: O CPF deve ser válido segundo o algoritmo de validação. Exemplos válidos:
   - `12345678909`
   - `98765432100`
   - `11122233344`

3. **Turnos Válidos**: 
   - `MANHA`
   - `TARDE`
   - `NOITE`

4. **Tipos de Pagamento Válidos**:
   - `DINHEIRO`
   - `CARTAO`
   - `PIX`

5. **Risco de Fraude**: Calculado automaticamente pela API:
   - DINHEIRO → BAIXO
   - PIX → BAIXO
   - CARTAO < 100 → BAIXO
   - CARTAO 100-500 → MEDIO
   - CARTAO > 500 → ALTO

6. **Data de Transação**: Preenchida automaticamente pela API no momento da criação

---

## 🚀 Como Usar no Postman

1. Importe o arquivo `CashPlusAssist-API.postman_collection.json` no Postman
2. Configure a variável `baseUrl` como `http://localhost:8080`
3. Execute as requisições conforme necessário

---

## 🔧 Variáveis de Ambiente Postman

```json
{
    "baseUrl": "http://localhost:8080",
    "sessionToken": ""
}
```

