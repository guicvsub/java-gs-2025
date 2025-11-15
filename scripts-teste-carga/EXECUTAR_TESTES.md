# Como Executar Testes de Carga

## Pré-requisitos

1. **API deve estar rodando** em `http://localhost:8080`
2. **Banco de dados** configurado e acessível

## Opção 1: Script Bash (Recomendado - Sem instalação)

O script bash funciona sem instalação de ferramentas adicionais:

```bash
# Teste básico (10 usuários, 10 requisições cada)
./scripts-teste-carga/bash/load-test.sh

# Teste com mais carga
CONCURRENT_USERS=20 REQUESTS_PER_USER=20 ./scripts-teste-carga/bash/load-test.sh

# Teste com URL customizada
BASE_URL=http://localhost:8080 ./scripts-teste-carga/bash/load-test.sh
```

### Variáveis de Ambiente

- `BASE_URL`: URL da API (padrão: http://localhost:8080)
- `CONCURRENT_USERS`: Número de usuários concorrentes (padrão: 10)
- `REQUESTS_PER_USER`: Requisições por usuário (padrão: 10)
- `DELAY_BETWEEN_REQUESTS`: Delay entre requisições em segundos (padrão: 0.5)

## Opção 2: k6 (Recomendado para testes avançados)

### Instalação

```bash
# Ubuntu/Debian
sudo gpg -k
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6

# Ou via snap
sudo snap install k6
```

### Executar

```bash
# Teste padrão
k6 run scripts-teste-carga/k6/load-test.js

# Com URL customizada
BASE_URL=http://localhost:8080 k6 run scripts-teste-carga/k6/load-test.js

# Com mais usuários
k6 run --vus 100 --duration 5m scripts-teste-carga/k6/load-test.js
```

## Opção 3: JMeter

### Instalação

```bash
# Ubuntu/Debian
sudo apt-get install jmeter

# Ou baixar de https://jmeter.apache.org/download_jmeter.cgi
```

### Executar

```bash
# Modo GUI (para configuração)
jmeter -t scripts-teste-carga/jmeter/load-test.jmx

# Modo não-GUI (para execução)
jmeter -n -t scripts-teste-carga/jmeter/load-test.jmx -l results.jtl -e -o report/

# Com propriedades customizadas
jmeter -n -t scripts-teste-carga/jmeter/load-test.jmx \
  -JbaseUrl=http://localhost:8080 \
  -l results.jtl \
  -e -o report/
```

## Interpretação dos Resultados

### Métricas Importantes

1. **Taxa de Sucesso**: Deve ser > 95%
2. **Tempo de Resposta Médio**: Deve ser < 500ms
3. **P95 (Percentil 95)**: 95% das requisições devem ser < 500ms
4. **Throughput (RPS)**: Requisições por segundo
5. **Taxa de Erro**: Deve ser < 5%

### Valores Esperados

- **Carga Normal**: 50 usuários simultâneos
  - Taxa de sucesso: > 98%
  - Tempo médio: < 200ms
  - RPS: > 50

- **Pico de Carga**: 100 usuários simultâneos
  - Taxa de sucesso: > 95%
  - Tempo médio: < 500ms
  - RPS: > 100

## Troubleshooting

### API não está respondendo

```bash
# Verificar se a API está rodando
curl http://localhost:8080/actuator/health

# Iniciar a API
mvn spring-boot:run
```

### Muitos erros 500

- Verifique os logs da aplicação
- Verifique se o banco de dados está acessível
- Verifique recursos do servidor (CPU, memória)

### Timeouts

- Aumente o timeout nas configurações do teste
- Verifique a performance do banco de dados
- Considere aumentar recursos do servidor

## Exemplo de Execução Completa

```bash
# 1. Iniciar a API
mvn spring-boot:run &

# 2. Aguardar API iniciar (30 segundos)
sleep 30

# 3. Executar teste de carga
./scripts-teste-carga/bash/load-test.sh

# 4. Ver resultados
cat /tmp/load-test-results.csv
```

