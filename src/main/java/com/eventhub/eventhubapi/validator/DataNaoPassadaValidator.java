package com.eventhub.eventhubapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementação do validador para {@link DataNaoPassada}.
 * Compara apenas o dia (LocalDate) com hoje; aceita o dia de hoje em qualquer horário.
 */
public class DataNaoPassadaValidator
        implements ConstraintValidator<DataNaoPassada, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.toLocalDate().isBefore(LocalDate.now());
    }
}
