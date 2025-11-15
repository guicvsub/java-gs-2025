package br.com.cashplus.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisição à API externa de risco de fraude.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiscoFraudeRequestDTO {
    
    private BigDecimal valor;
    private String tipoPagamento;
}


