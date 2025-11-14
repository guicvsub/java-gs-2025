package br.com.cashplus.model;

import br.com.cashplus.validation.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    
    @NotBlank(message = "Turno é obrigatório")
    @Column(nullable = false, length = 10)
    private String turno; // MANHA, TARDE, NOITE
}

