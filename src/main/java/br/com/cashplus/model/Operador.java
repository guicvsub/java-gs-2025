package br.com.cashplus.model;

import br.com.cashplus.model.enums.TurnoEnum;
import br.com.cashplus.model.valueobject.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @NotNull(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true, length = 11)
    @Convert(converter = CPFConverter.class)
    private CPF cpf;
    
    @NotNull(message = "Turno é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private TurnoEnum turno;
    
    @Converter
    public static class CPFConverter implements AttributeConverter<CPF, String> {
        @Override
        public String convertToDatabaseColumn(CPF cpf) {
            return cpf != null ? cpf.getValor() : null;
        }
        
        @Override
        public CPF convertToEntityAttribute(String dbData) {
            return dbData != null ? CPF.of(dbData) : null;
        }
    }
}

