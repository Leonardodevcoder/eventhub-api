package com.eventhub.eventhubapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do evento não pode ser vazio")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "Data do evento é obrigatória")
    @DataNaoPassada(message = "Data do evento não pode ser no passado")
    @Column(nullable = false)
    private LocalDateTime data;

    @NotBlank(message = "Local do evento não pode ser vazio")
    @Column(nullable = false)
    private String local;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser maior que zero")
    @Column(nullable = false)
    private Integer capacidade;
}
