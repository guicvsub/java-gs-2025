package br.com.cashplus.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta da API externa de risco de fraude.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiscoFraudeResponseDTO {
    
    private String risco;
    private String mensagem;
    private Integer score;
}


