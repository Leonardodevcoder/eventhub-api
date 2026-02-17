package com.eventhub.eventhubapi.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que a data (LocalDateTime) não está no passado.
 * Compara apenas o dia (LocalDate) com a data atual, aceitando qualquer horário do dia presente.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataNaoPassadaValidator.class)
@Documented
public @interface DataNaoPassada {

    String message() default "A data não pode estar no passado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
