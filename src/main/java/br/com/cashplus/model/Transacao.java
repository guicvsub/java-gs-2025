package br.com.cashplus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @NotBlank(message = "Tipo de pagamento é obrigatório")
    @Column(nullable = false, length = 20)
    private String tipoPagamento; // DINHEIRO, CARTAO, PIX
    
    @Column(length = 20)
    private String riscoFraude; // BAIXO, MEDIO, ALTO (calculado automaticamente)
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataTransacao;
    
    @PrePersist
    protected void onCreate() {
        dataTransacao = LocalDateTime.now();
    }
}

