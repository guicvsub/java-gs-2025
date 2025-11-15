package br.com.cashplus.model.enums;

/**
 * Enum que representa os níveis de risco de fraude.
 */
public enum RiscoFraudeEnum {
    BAIXO("Baixo"),
    MEDIO("Médio"),
    ALTO("Alto");
    
    private final String descricao;
    
    RiscoFraudeEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}


