package br.com.cashplus.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    
    private Class<? extends Enum<?>> enumClass;
    
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotBlank já valida isso
        }
        
        try {
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants != null) {
                for (Enum<?> enumConstant : enumConstants) {
                    if (enumConstant.name().equalsIgnoreCase(value)) {
                        return true;
                    }
                }
            }
            
            // Construir mensagem com valores aceitos
            if (context != null) {
                StringBuilder acceptedValues = new StringBuilder();
                for (int i = 0; i < enumConstants.length; i++) {
                    if (i > 0) acceptedValues.append(" / ");
                    acceptedValues.append(enumConstants[i].name());
                }
                
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Valor inválido. Valores aceitos: " + acceptedValues.toString()
                ).addConstraintViolation();
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }
}

