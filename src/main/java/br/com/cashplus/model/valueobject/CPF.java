package br.com.cashplus.model.valueobject;

import java.util.Objects;

/**
 * Value Object que representa um CPF.
 * Imutável e encapsula a lógica de validação e formatação.
 */
public final class CPF {
    
    private final String valor;
    
    private CPF(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria um CPF a partir de uma string, removendo caracteres não numéricos.
     * @param cpf String com o CPF (pode conter formatação)
     * @return Instância de CPF
     * @throws IllegalArgumentException se o CPF for inválido
     */
    public static CPF of(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }
        
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if (!isValid(cpfLimpo)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        
        return new CPF(cpfLimpo);
    }
    
    /**
     * Valida se um CPF é válido.
     */
    private static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação dos dígitos verificadores
        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }
            
            // Calcula primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) {
                firstDigit = 0;
            }
            
            if (firstDigit != digits[9]) {
                return false;
            }
            
            // Calcula segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) {
                secondDigit = 0;
            }
            
            return secondDigit == digits[10];
            
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Retorna o CPF sem formatação (apenas números).
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Retorna o CPF formatado (XXX.XXX.XXX-XX).
     */
    public String getFormatado() {
        if (valor.length() != 11) {
            return valor;
        }
        return valor.substring(0, 3) + "." + 
               valor.substring(3, 6) + "." + 
               valor.substring(6, 9) + "-" + 
               valor.substring(9, 11);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(valor, cpf.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return getFormatado();
    }
}


