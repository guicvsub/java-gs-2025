-- V1__Initial_Schema.sql

CREATE TABLE operadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    turno VARCHAR(10) NOT NULL,
    CONSTRAINT chk_turno CHECK (turno IN ('MANHA', 'TARDE', 'NOITE'))
);

CREATE TABLE transacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    valor DECIMAL(10, 2) NOT NULL,
    tipo_pagamento VARCHAR(20) NOT NULL,
    risco_fraude VARCHAR(20),
    operador_id BIGINT,
    data_transacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tipo_pagamento CHECK (tipo_pagamento IN ('DINHEIRO', 'CARTAO', 'PIX')),
    CONSTRAINT chk_risco_fraude CHECK (risco_fraude IS NULL OR risco_fraude IN ('BAIXO', 'MEDIO', 'ALTO')),
    CONSTRAINT fk_transacao_operador FOREIGN KEY (operador_id) REFERENCES operadores(id) ON DELETE SET NULL,
    INDEX idx_operador_id (operador_id),
    INDEX idx_data_transacao (data_transacao)
);
