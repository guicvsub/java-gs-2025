package br.com.cashplus.dto.request;

import br.com.cashplus.model.enums.TurnoEnum;
import br.com.cashplus.validation.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para entrada de dados de Operador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;
    
    @NotNull(message = "Turno é obrigatório")
    private TurnoEnum turno;
}


