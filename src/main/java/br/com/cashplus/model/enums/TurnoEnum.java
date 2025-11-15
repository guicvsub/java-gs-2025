package br.com.cashplus.model.enums;

/**
 * Enum que representa os turnos de trabalho dos operadores.
 */
public enum TurnoEnum {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");
    
    private final String descricao;
    
    TurnoEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}


