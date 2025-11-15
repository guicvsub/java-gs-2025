# Exemplos de cURL - CashPlusAssist API

## 🔐 1. Criar Sessão (Obtém Token)

```bash
curl -X POST "http://localhost:8080/sessao/criar?userId=operador123"
```

**Resposta:**
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "message": "Sessão criada com sucesso"
}
```

**Salvar token em variável:**
```bash
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=operador123" | jq -r '.token')
echo "Token: $TOKEN"
```

---

## 👤 2. Criar Operador

```bash
curl -X POST "http://localhost:8080/api/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva",
    "cpf": "111.444.777-35",
    "turno": "MANHA"
  }'
```

**Com token direto:**
```bash
curl -X POST "http://localhost:8080/api/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: SEU_TOKEN_AQUI" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "987.654.321-00",
    "turno": "TARDE"
  }'
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

---

## 📋 3. Listar Operadores

```bash
curl -X GET "http://localhost:8080/api/operadores" \
  -H "X-Session-Token: $TOKEN"
```

---

## 🔍 4. Buscar Operador por ID

```bash
curl -X GET "http://localhost:8080/api/operadores/1" \
  -H "X-Session-Token: $TOKEN"
```

---

## ✏️ 5. Atualizar Operador

```bash
curl -X PUT "http://localhost:8080/api/operadores/1" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva Santos",
    "cpf": "111.444.777-35",
    "turno": "TARDE"
  }'
```

---

## 🗑️ 6. Deletar Operador

```bash
curl -X DELETE "http://localhost:8080/api/operadores/1" \
  -H "X-Session-Token: $TOKEN"
```

---

## 💰 7. Criar Transação

```bash
curl -X POST "http://localhost:8080/api/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 150.50,
    "tipoPagamento": "CARTAO",
    "operadorId": 1
  }'
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
  "dataTransacao": "2025-11-15T10:00:00"
}
```

---

## 📋 8. Listar Transações

```bash
curl -X GET "http://localhost:8080/api/transacoes" \
  -H "X-Session-Token: $TOKEN"
```

---

## 🔍 9. Buscar Transação por ID

```bash
curl -X GET "http://localhost:8080/api/transacoes/1" \
  -H "X-Session-Token: $TOKEN"
```

---

## 🗑️ 10. Deletar Transação

```bash
curl -X DELETE "http://localhost:8080/api/transacoes/1" \
  -H "X-Session-Token: $TOKEN"
```

---

## 📝 Valores Aceitos

### Turno (Operador)
- `MANHA`
- `TARDE`
- `NOITE`

### Tipo de Pagamento (Transação)
- `DINHEIRO`
- `CARTAO`
- `PIX`

---

## 🚀 Fluxo Completo (Exemplo)

```bash
# 1. Criar sessão e salvar token
TOKEN=$(curl -s -X POST "http://localhost:8080/sessao/criar?userId=operador1" | jq -r '.token')

# 2. Criar operador
curl -X POST "http://localhost:8080/api/operadores" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "nome": "João Silva",
    "cpf": "111.444.777-35",
    "turno": "MANHA"
  }'

# 3. Criar transação associada
curl -X POST "http://localhost:8080/api/transacoes" \
  -H "Content-Type: application/json" \
  -H "X-Session-Token: $TOKEN" \
  -d '{
    "valor": 250.00,
    "tipoPagamento": "CARTAO",
    "operadorId": 1
  }'

# 4. Listar transações
curl -X GET "http://localhost:8080/api/transacoes" \
  -H "X-Session-Token: $TOKEN"
```

---

## 💡 Dicas

1. **Formatação JSON**: Use `jq` para formatar a resposta:
   ```bash
   curl ... | jq
   ```

2. **Verbose**: Adicione `-v` para ver headers completos:
   ```bash
   curl -v ...
   ```

3. **Salvar resposta**: Redirecione para arquivo:
   ```bash
   curl ... > resposta.json
   ```

4. **CPF válido para testes**: `111.444.777-35`

