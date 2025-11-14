package br.com.cashplus.dto;

import br.com.cashplus.validation.CPF;
import br.com.cashplus.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadorDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;
    
    @NotBlank(message = "Turno é obrigatório")
    @EnumValue(enumClass = TurnoEnum.class, message = "Turno deve ser MANHA, TARDE ou NOITE")
    private String turno;
    
    public enum TurnoEnum {
        MANHA, TARDE, NOITE
    }
}

