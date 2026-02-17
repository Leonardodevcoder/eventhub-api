package com.eventhub.eventhubapi.dto;

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
public class IngressoResponse {

    private Long id;
    private Long eventoId;
    private String nomeEvento;
    private Long participanteId;
    private String nomeParticipante;
    private LocalDateTime dataCompra;
}
