package br.com.cashplus.dto.request;

import br.com.cashplus.model.enums.TipoPagamentoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para entrada de dados de Transação.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequestDTO {
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    private TipoPagamentoEnum tipoPagamento;
    
    private Long operadorId; // Opcional
}


