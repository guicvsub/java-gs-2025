package br.com.cashplus.dto.response;

import br.com.cashplus.model.enums.RiscoFraudeEnum;
import br.com.cashplus.model.enums.TipoPagamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para saída de dados de Transação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoResponseDTO {
    
    private Long id;
    private BigDecimal valor;
    private TipoPagamentoEnum tipoPagamento;
    private String tipoPagamentoDescricao;
    private RiscoFraudeEnum riscoFraude;
    private String riscoFraudeDescricao;
    private Long operadorId;
    private String operadorNome;
    private LocalDateTime dataTransacao;
}


