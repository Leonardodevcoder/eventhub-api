package com.eventhub.eventhubapi.dto;

import com.eventhub.eventhubapi.validator.DataNaoPassada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoRequest {

    @NotBlank(message = "Nome do evento não pode ser vazio")
    private String nome;

    @NotNull(message = "Data do evento é obrigatória")
    @DataNaoPassada(message = "Data do evento não pode ser no passado")
    private LocalDateTime data;

    @NotBlank(message = "Local do evento não pode ser vazio")
    private String local;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser maior que zero")
    private Integer capacidade;
}
