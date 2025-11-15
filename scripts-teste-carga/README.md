# Testes de Carga - CashPlusAssist API

Este diretório contém scripts para testes de carga e performance da API.

## Ferramentas Disponíveis

### 1. JMeter

O arquivo `jmeter/load-test.jmx` contém um plano de teste configurado para:
- **Operadores**: 50 threads, 10 loops, ramp-up de 30s
- **Transações**: 100 threads, 20 loops, ramp-up de 60s

#### Como executar:

```bash
# Modo GUI
jmeter -t scripts-teste-carga/jmeter/load-test.jmx

# Modo não-GUI (recomendado para testes)
jmeter -n -t scripts-teste-carga/jmeter/load-test.jmx -l results.jtl -e -o report/

# Com propriedades customizadas
jmeter -n -t scripts-teste-carga/jmeter/load-test.jmx \
  -JbaseUrl=http://localhost:8080 \
  -l results.jtl \
  -e -o report/
```

#### Interpretação dos Resultados:

- **Summary Report**: Mostra estatísticas gerais (média, min, max, desvio padrão)
- **Response Time**: Tempo de resposta das requisições
- **Throughput**: Requisições por segundo
- **Error Rate**: Taxa de erros

### 2. k6

O arquivo `k6/load-test.js` contém um script de teste configurado com:
- **Stages**: Ramp-up gradual de 50 para 100 usuários
- **Thresholds**: 
  - 95% das requisições < 500ms
  - Taxa de erro < 5%

#### Como executar:

```bash
# Instalar k6 (se necessário)
# Linux: https://k6.io/docs/getting-started/installation/

# Executar teste
k6 run scripts-teste-carga/k6/load-test.js

# Com URL customizada
BASE_URL=http://localhost:8080 k6 run scripts-teste-carga/k6/load-test.js

# Com mais opções
k6 run --vus 100 --duration 5m scripts-teste-carga/k6/load-test.js
```

#### Interpretação dos Resultados:

- **http_req_duration**: Tempo de resposta (avg, min, max, p95, p99)
- **http_req_failed**: Taxa de requisições falhadas
- **http_reqs**: Total de requisições e taxa (req/s)
- **errors**: Taxa de erros customizada

## Cenários de Teste

### Cenário 1: Carga Normal
- **Usuários**: 50 simultâneos
- **Duração**: 5 minutos
- **Objetivo**: Validar comportamento sob carga normal

### Cenário 2: Pico de Carga
- **Usuários**: 100 simultâneos
- **Duração**: 10 minutos
- **Objetivo**: Validar comportamento sob pico de tráfego

### Cenário 3: Stress Test
- **Usuários**: 200+ simultâneos
- **Duração**: 15 minutos
- **Objetivo**: Identificar limites do sistema

## Métricas Importantes

1. **Response Time (Tempo de Resposta)**
   - Média: < 200ms
   - P95: < 500ms
   - P99: < 1000ms

2. **Throughput (Taxa de Requisições)**
   - Requisições por segundo (RPS)
   - Objetivo: > 100 RPS

3. **Error Rate (Taxa de Erro)**
   - Deve ser < 1% em condições normais
   - < 5% em condições de stress

4. **Concorrência**
   - Número de usuários simultâneos suportados
   - Objetivo: > 100 usuários simultâneos

## Dicas

1. **Antes de executar**: Certifique-se de que a API está rodando e o banco de dados está configurado
2. **Monitoramento**: Monitore CPU, memória e conexões do banco durante os testes
3. **Ambiente**: Execute testes em ambiente isolado, não em produção
4. **Análise**: Compare resultados entre diferentes versões da API

## Troubleshooting

### Erros de Conexão
- Verifique se a API está rodando na porta correta
- Verifique firewall e configurações de rede

### Timeouts
- Aumente o timeout nas configurações do teste
- Verifique se o banco de dados está respondendo

### Alta Taxa de Erro
- Verifique logs da aplicação
- Monitore recursos do servidor (CPU, memória)
- Verifique conexões do banco de dados


