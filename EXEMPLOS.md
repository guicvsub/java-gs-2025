# Exemplos de Uso da API

Este documento contém exemplos práticos de como usar a API CashPlusAssist.

## 🔐 1. Criar Sessão

Primeiro, você precisa criar uma sessão para obter um token de autenticação.

### cURL
```bash
curl -X POST "http://localhost:8080/sessao/criar?userId=operador123"
```

### Resposta
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Sessão criada com sucesso"
}
```

### JavaScript (Fetch)
```javascript
const response = await fetch('http://localhost:8080/sessao/criar?userId=operador123', {
  method: 'POST'
});
const data = await response.json();
const token = data.token;
```

---

## 👤 2. Criar Operador

### cURL
```bash
TOKEN="seu-token-aqui"

curl -X POST "http://localhost:8080/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
  }'
```

### JavaScript (Fetch)
```javascript
const operador = {
  nome: "João Silva",
  cpf: "12345678909",
  turno: "MANHA"
};

const response = await fetch('http://localhost:8080/operadores', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Session-Token': token
  },
  body: JSON.stringify(operador)
});

const resultado = await response.json();
console.log(resultado);
```

### Resposta (201 Created)
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678909",
  "turno": "MANHA"
}
```

---

## 📋 3. Listar Operadores

### cURL
```bash
curl -X GET "http://localhost:8080/operadores" \
  -H "X-Session-Token: $TOKEN"
```

### JavaScript (Fetch)
```javascript
const response = await fetch('http://localhost:8080/operadores', {
  headers: {
    'X-Session-Token': token
  }
});

const operadores = await response.json();
console.log(operadores);
```

### Resposta (200 OK)
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

## 🔍 4. Buscar Operador por ID

### cURL
```bash
curl -X GET "http://localhost:8080/operadores/1" \
  -H "X-Session-Token: $TOKEN"
```

### JavaScript (Fetch)
```javascript
const response = await fetch('http://localhost:8080/operadores/1', {
  headers: {
    'X-Session-Token': token
  }
});

const operador = await response.json();
console.log(operador);
```

---

## ✏️ 5. Atualizar Operador

### cURL
```bash
curl -X PUT "http://localhost:8080/operadores/1" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva Santos",
    "cpf": "12345678909",
    "turno": "TARDE"
  }'
```

### JavaScript (Fetch)
```javascript
const operadorAtualizado = {
  nome: "João Silva Santos",
  cpf: "12345678909",
  turno: "TARDE"
};

const response = await fetch('http://localhost:8080/operadores/1', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
    'X-Session-Token': token
  },
  body: JSON.stringify(operadorAtualizado)
});

const resultado = await response.json();
```

---

## 🗑️ 6. Deletar Operador

### cURL
```bash
curl -X DELETE "http://localhost:8080/operadores/1" \
  -H "X-Session-Token: $TOKEN"
```

### JavaScript (Fetch)
```javascript
const response = await fetch('http://localhost:8080/operadores/1', {
  method: 'DELETE',
  headers: {
    'X-Session-Token': token
  }
});

if (response.status === 204) {
  console.log('Operador deletado com sucesso');
}
```

---

## 💰 7. Criar Transação

### Exemplo 1: Pagamento em Dinheiro (Risco BAIXO)

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 50.00,
    "tipoPagamento": "DINHEIRO"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "valor": 50.00,
  "tipoPagamento": "DINHEIRO",
  "riscoFraude": "BAIXO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

### Exemplo 2: Pagamento com Cartão - Valor Baixo (Risco BAIXO)

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 80.00,
    "tipoPagamento": "CARTAO"
  }'
```

**Resposta:**
```json
{
  "id": 2,
  "valor": 80.00,
  "tipoPagamento": "CARTAO",
  "riscoFraude": "BAIXO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

### Exemplo 3: Pagamento com Cartão - Valor Médio (Risco MEDIO)

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 250.00,
    "tipoPagamento": "CARTAO"
  }'
```

**Resposta:**
```json
{
  "id": 3,
  "valor": 250.00,
  "tipoPagamento": "CARTAO",
  "riscoFraude": "MEDIO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

### Exemplo 4: Pagamento com Cartão - Valor Alto (Risco ALTO)

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 750.00,
    "tipoPagamento": "CARTAO"
  }'
```

**Resposta:**
```json
{
  "id": 4,
  "valor": 750.00,
  "tipoPagamento": "CARTAO",
  "riscoFraude": "ALTO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

### Exemplo 5: Pagamento PIX (Risco BAIXO)

```bash
curl -X POST "http://localhost:8080/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 300.00,
    "tipoPagamento": "PIX"
  }'
```

**Resposta:**
```json
{
  "id": 5,
  "valor": 300.00,
  "tipoPagamento": "PIX",
  "riscoFraude": "BAIXO",
  "dataTransacao": "2025-01-14T16:40:32"
}
```

---

## 📋 8. Listar Transações

### cURL
```bash
curl -X GET "http://localhost:8080/transacoes" \
  -H "X-Session-Token: $TOKEN"
```

### JavaScript (Fetch)
```javascript
const response = await fetch('http://localhost:8080/transacoes', {
  headers: {
    'X-Session-Token': token
  }
});

const transacoes = await response.json();
console.log(transacoes);
```

---

## 🚨 9. Exemplos de Erros

### Erro de Validação

**Requisição Inválida:**
```bash
curl -X POST "http://localhost:8080/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "AB",
    "cpf": "123",
    "turno": "INVALIDO"
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "timestamp": "2025-01-14T16:40:32",
  "status": 400,
  "error": "Validation Error",
  "messages": [
    "nome: Nome deve ter no mínimo 3 caracteres",
    "cpf: CPF inválido",
    "turno: Valor inválido. Valores aceitos: MANHA / TARDE / NOITE"
  ],
  "path": "/operadores"
}
```

### Erro de Sessão Inválida

**Requisição sem Token:**
```bash
curl -X GET "http://localhost:8080/operadores"
```

**Resposta (401 Unauthorized):**
```json
{
  "timestamp": "2025-01-14T16:40:32",
  "status": 401,
  "error": "Session Error",
  "messages": [
    "Token de sessão é obrigatório. Envie o header X-Session-Token"
  ],
  "path": "/operadores"
}
```

### Erro de Recurso Não Encontrado

**Requisição com ID Inexistente:**
```bash
curl -X GET "http://localhost:8080/operadores/999" \
  -H "X-Session-Token: $TOKEN"
```

**Resposta (404 Not Found):**
```json
{
  "timestamp": "2025-01-14T16:40:32",
  "status": 404,
  "error": "Resource Not Found",
  "messages": [
    "Operador não encontrado com ID: 999"
  ],
  "path": "/operadores/999"
}
```

---

## 🔄 10. Fluxo Completo de Uso

### Script Bash Completo

```bash
#!/bin/bash

API_URL="http://localhost:8080"

# 1. Criar sessão
echo "1. Criando sessão..."
SESSION_RESPONSE=$(curl -s -X POST "$API_URL/sessao/criar?userId=operador123")
TOKEN=$(echo $SESSION_RESPONSE | jq -r '.token')
echo "Token obtido: $TOKEN"
echo ""

# 2. Criar operador
echo "2. Criando operador..."
OPERADOR_RESPONSE=$(curl -s -X POST "$API_URL/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678909",
    "turno": "MANHA"
  }')
OPERADOR_ID=$(echo $OPERADOR_RESPONSE | jq -r '.id')
echo "Operador criado com ID: $OPERADOR_ID"
echo ""

# 3. Criar transação
echo "3. Criando transação..."
TRANSACAO_RESPONSE=$(curl -s -X POST "$API_URL/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 150.50,
    "tipoPagamento": "CARTAO"
  }')
TRANSACAO_ID=$(echo $TRANSACAO_RESPONSE | jq -r '.id')
RISCO=$(echo $TRANSACAO_RESPONSE | jq -r '.riscoFraude')
echo "Transação criada com ID: $TRANSACAO_ID"
echo "Risco de fraude: $RISCO"
echo ""

# 4. Listar operadores
echo "4. Listando operadores..."
curl -s -X GET "$API_URL/operadores" \
  -H "X-Session-Token: $TOKEN" | jq '.'
echo ""

# 5. Listar transações
echo "5. Listando transações..."
curl -s -X GET "$API_URL/transacoes" \
  -H "X-Session-Token: $TOKEN" | jq '.'
```

---

## 📝 Notas Importantes

1. **Token de Sessão**: Todas as requisições (exceto criação de sessão) precisam do header `X-Session-Token`
2. **Validade do Token**: Tokens expiram após 30 minutos de inatividade
3. **Cálculo de Risco**: O campo `riscoFraude` é calculado automaticamente pelo serviço
4. **Validações**: Todos os campos são validados antes de serem processados
5. **CPF**: Deve ser um CPF válido com dígitos verificadores corretos

---

**Para mais informações, consulte o [README.md](README.md)**

