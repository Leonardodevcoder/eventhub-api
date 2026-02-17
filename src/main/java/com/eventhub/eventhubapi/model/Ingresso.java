package com.eventhub.eventhubapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingresso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participante participante;

    @Builder.Default
    private LocalDateTime dataCompra = LocalDateTime.now();
}
