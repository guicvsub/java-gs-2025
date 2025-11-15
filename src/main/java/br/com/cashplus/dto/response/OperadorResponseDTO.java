package br.com.cashplus.dto.response;

import br.com.cashplus.model.enums.TurnoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para saída de dados de Operador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorResponseDTO {
    
    private Long id;
    private String nome;
    private String cpf;
    private TurnoEnum turno;
    private String turnoDescricao;
}


