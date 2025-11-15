#!/bin/bash

# Script de Teste de Carga Simples usando curl
# Para testes mais avançados, instale k6 ou JMeter

BASE_URL="${BASE_URL:-http://localhost:8080}"
CONCURRENT_USERS="${CONCURRENT_USERS:-10}"
REQUESTS_PER_USER="${REQUESTS_PER_USER:-10}"
DELAY_BETWEEN_REQUESTS="${DELAY_BETWEEN_REQUESTS:-0.5}"

echo "=========================================="
echo "  TESTE DE CARGA - CashPlusAssist API"
echo "=========================================="
echo "URL Base: $BASE_URL"
echo "Usuários Concorrentes: $CONCURRENT_USERS"
echo "Requisições por Usuário: $REQUESTS_PER_USER"
echo "Delay entre Requisições: ${DELAY_BETWEEN_REQUESTS}s"
echo "=========================================="
echo ""

# Verificar se a API está rodando
echo "Verificando se a API está rodando..."
if ! curl -s -f -X POST "$BASE_URL/sessao/criar?userId=healthcheck" > /dev/null 2>&1; then
    echo "⚠️  API não está respondendo em $BASE_URL"
    echo "   Certifique-se de que a aplicação está rodando!"
    exit 1
fi
echo "✅ API está respondendo"
echo ""

# Criar sessão
echo "Criando sessão de teste..."
SESSION_RESPONSE=$(curl -s -X POST "$BASE_URL/sessao/criar?userId=loadtest-$(date +%s)")
TOKEN=$(echo $SESSION_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ Erro ao criar sessão"
    exit 1
fi
echo "✅ Sessão criada: ${TOKEN:0:20}..."
echo ""

# Função para gerar CPF válido
gerar_cpf_valido() {
    # Gera 9 dígitos aleatórios
    local base=$(printf "%09d" $((RANDOM * RANDOM % 1000000000)))
    
    # Calcula primeiro dígito verificador
    local sum=0
    for i in {0..8}; do
        local digit=${base:$i:1}
        sum=$((sum + digit * (10 - i)))
    done
    local first_digit=$((11 - (sum % 11)))
    if [ $first_digit -ge 10 ]; then
        first_digit=0
    fi
    
    # Calcula segundo dígito verificador
    sum=0
    for i in {0..8}; do
        local digit=${base:$i:1}
        sum=$((sum + digit * (11 - i)))
    done
    sum=$((sum + first_digit * 2))
    local second_digit=$((11 - (sum % 11)))
    if [ $second_digit -ge 10 ]; then
        second_digit=0
    fi
    
    echo "${base}${first_digit}${second_digit}"
}

# Função para fazer requisições
make_request() {
    local user_id=$1
    local request_num=$2
    local start_time=$(date +%s.%N)
    
    # Criar operador com CPF válido
    local cpf=$(gerar_cpf_valido)
    local response=$(curl -s -w "\n%{http_code}\n%{time_total}" \
        -X POST "$BASE_URL/api/operadores" \
        -H "Content-Type: application/json" \
        -H "X-Session-Token: $TOKEN" \
        -d "{
            \"nome\": \"Operador Load Test $user_id-$request_num\",
            \"cpf\": \"$cpf\",
            \"turno\": \"MANHA\"
        }")
    
    local end_time=$(date +%s.%N)
    local duration=$(echo "$end_time - $start_time" | bc)
    local http_code=$(echo "$response" | tail -2 | head -1)
    local time_total=$(echo "$response" | tail -1)
    
    echo "$user_id,$request_num,$http_code,$time_total,$duration"
    
    sleep $DELAY_BETWEEN_REQUESTS
}

# Executar testes
echo "Iniciando testes de carga..."
echo "Usuário,Requisição,HTTP Code,Tempo Total(s),Duração(s)" > /tmp/load-test-results.csv

# Executar requisições em paralelo
for user in $(seq 1 $CONCURRENT_USERS); do
    (
        for req in $(seq 1 $REQUESTS_PER_USER); do
            make_request $user $req >> /tmp/load-test-results.csv
        done
    ) &
done

# Aguardar todos os processos terminarem
wait

echo ""
echo "=========================================="
echo "  RESULTADOS DO TESTE"
echo "=========================================="

# Analisar resultados
TOTAL_REQUESTS=$(tail -n +2 /tmp/load-test-results.csv | wc -l)
SUCCESS_REQUESTS=$(tail -n +2 /tmp/load-test-results.csv | awk -F',' '$3 == 201 || $3 == 200' | wc -l)
FAILED_REQUESTS=$((TOTAL_REQUESTS - SUCCESS_REQUESTS))
SUCCESS_RATE=$(echo "scale=2; $SUCCESS_REQUESTS * 100 / $TOTAL_REQUESTS" | bc)
ERROR_RATE=$(echo "scale=2; $FAILED_REQUESTS * 100 / $TOTAL_REQUESTS" | bc)

# Calcular estatísticas de tempo
AVG_TIME=$(tail -n +2 /tmp/load-test-results.csv | awk -F',' '{sum+=$4; count++} END {if(count>0) print sum/count; else print 0}')
MIN_TIME=$(tail -n +2 /tmp/load-test-results.csv | awk -F',' 'NR==2{min=$4} {if($4<min) min=$4} END {print min}')
MAX_TIME=$(tail -n +2 /tmp/load-test-results.csv | awk -F',' 'NR==2{max=$4} {if($4>max) max=$4} END {print max}')

echo "Total de Requisições: $TOTAL_REQUESTS"
echo "Requisições Bem-sucedidas: $SUCCESS_REQUESTS ($SUCCESS_RATE%)"
echo "Requisições com Erro: $FAILED_REQUESTS ($ERROR_RATE%)"
echo ""
echo "Tempo de Resposta:"
echo "  Média: ${AVG_TIME}s"
echo "  Mínimo: ${MIN_TIME}s"
echo "  Máximo: ${MAX_TIME}s"
echo ""

# Calcular requisições por segundo
TOTAL_DURATION=$(tail -n +2 /tmp/load-test-results.csv | awk -F',' '{sum+=$4} END {if(sum>0) print sum; else print 1}')
if [ "$TOTAL_DURATION" != "0" ] && [ -n "$TOTAL_DURATION" ]; then
    RPS=$(echo "scale=2; $TOTAL_REQUESTS / $TOTAL_DURATION" | bc)
    echo "Throughput: ${RPS} requisições/segundo"
else
    echo "Throughput: N/A (duração total muito pequena)"
fi
echo ""

# Mostrar códigos de status HTTP
echo "Distribuição de Códigos HTTP:"
tail -n +2 /tmp/load-test-results.csv | awk -F',' '{count[$3]++} END {for (code in count) print "  " code ": " count[code]}'
echo ""

echo "Arquivo de resultados: /tmp/load-test-results.csv"
echo "=========================================="

# Verificar se há muitos erros
if (( $(echo "$ERROR_RATE > 5" | bc -l) )); then
    echo "⚠️  ATENÇÃO: Taxa de erro acima de 5%!"
    exit 1
else
    echo "✅ Teste concluído com sucesso!"
    exit 0
fi

