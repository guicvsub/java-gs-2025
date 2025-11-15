package br.com.cashplus.model.enums;

/**
 * Enum que representa os tipos de pagamento disponíveis.
 */
public enum TipoPagamentoEnum {
    DINHEIRO("Dinheiro"),
    CARTAO("Cartão"),
    PIX("PIX");
    
    private final String descricao;
    
    TipoPagamentoEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}


