import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// Métricas customizadas
const errorRate = new Rate('errors');

// Configuração do teste
export const options = {
  stages: [
    { duration: '30s', target: 50 },   // Ramp-up: 50 usuários em 30s
    { duration: '1m', target: 50 },     // Mantém 50 usuários por 1 minuto
    { duration: '30s', target: 100 },   // Aumenta para 100 usuários em 30s
    { duration: '1m', target: 100 },     // Mantém 100 usuários por 1 minuto
    { duration: '30s', target: 0 },     // Ramp-down: reduz para 0 em 30s
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% das requisições devem ser < 500ms
    http_req_failed: ['rate<0.05'],     // Taxa de erro deve ser < 5%
    errors: ['rate<0.05'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// Função para gerar CPF aleatório (apenas para teste)
function gerarCPF() {
  const random = Math.floor(Math.random() * 10000000000);
  return random.toString().padStart(11, '0');
}

export default function () {
  // Teste 1: Criar Operador
  const operadorPayload = JSON.stringify({
    nome: `Operador Teste ${__VU}-${__ITER}`,
    cpf: gerarCPF(),
    turno: 'MANHA'
  });

  const operadorParams = {
    headers: { 'Content-Type': 'application/json' },
  };

  const operadorRes = http.post(
    `${BASE_URL}/api/operadores`,
    operadorPayload,
    operadorParams
  );

  const operadorSuccess = check(operadorRes, {
    'operador criado com sucesso': (r) => r.status === 201,
    'tempo de resposta < 500ms': (r) => r.timings.duration < 500,
  });

  errorRate.add(!operadorSuccess);
  sleep(1);

  // Teste 2: Listar Operadores
  const listarRes = http.get(`${BASE_URL}/api/operadores`);

  const listarSuccess = check(listarRes, {
    'listar operadores OK': (r) => r.status === 200,
    'tempo de resposta < 500ms': (r) => r.timings.duration < 500,
  });

  errorRate.add(!listarSuccess);
  sleep(1);

  // Teste 3: Criar Transação
  const transacaoPayload = JSON.stringify({
    valor: Math.random() * 1000 + 10,
    tipoPagamento: 'CARTAO',
  });

  const transacaoRes = http.post(
    `${BASE_URL}/api/transacoes`,
    transacaoPayload,
    operadorParams
  );

  const transacaoSuccess = check(transacaoRes, {
    'transacao criada com sucesso': (r) => r.status === 201,
    'tempo de resposta < 500ms': (r) => r.timings.duration < 500,
  });

  errorRate.add(!transacaoSuccess);
  sleep(1);

  // Teste 4: Listar Transações
  const listarTransacoesRes = http.get(`${BASE_URL}/api/transacoes`);

  const listarTransacoesSuccess = check(listarTransacoesRes, {
    'listar transacoes OK': (r) => listarTransacoesRes.status === 200,
    'tempo de resposta < 500ms': (r) => r.timings.duration < 500,
  });

  errorRate.add(!listarTransacoesSuccess);
  sleep(1);
}

export function handleSummary(data) {
  return {
    'stdout': textSummary(data, { indent: ' ', enableColors: true }),
    'summary.json': JSON.stringify(data),
  };
}

function textSummary(data, options) {
  return `
    =====================
     RESUMO DO TESTE
    =====================
    Duração: ${data.state.testRunDurationMs / 1000}s
    Requisições: ${data.metrics.http_reqs.values.count}
    Taxa de erro: ${(data.metrics.http_req_failed.values.rate * 100).toFixed(2)}%
    Tempo médio: ${data.metrics.http_req_duration.values.avg.toFixed(2)}ms
    P95: ${data.metrics.http_req_duration.values['p(95)'].toFixed(2)}ms
    P99: ${data.metrics.http_req_duration.values['p(99)'].toFixed(2)}ms
    =====================
  `;
}


