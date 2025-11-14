package br.com.cashplus.dto;

import br.com.cashplus.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoDTO {
    
    private Long id;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;
    
    @NotBlank(message = "Tipo de pagamento é obrigatório")
    @EnumValue(enumClass = TipoPagamentoEnum.class, message = "Tipo de pagamento deve ser DINHEIRO, CARTAO ou PIX")
    private String tipoPagamento;
    
    private String riscoFraude;
    
    private LocalDateTime dataTransacao;
    
    public enum TipoPagamentoEnum {
        DINHEIRO, CARTAO, PIX
    }
}

