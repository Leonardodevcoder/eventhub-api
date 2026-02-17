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
public class EventoResponse {

    private Long id;
    private String nome;
    private LocalDateTime data;
    private String local;
    private Integer capacidade;
}
