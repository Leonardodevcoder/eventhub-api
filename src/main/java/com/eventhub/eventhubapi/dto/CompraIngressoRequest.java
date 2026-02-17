package com.eventhub.eventhubapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraIngressoRequest {

    @NotNull(message = "ID do evento é obrigatório")
    private Long eventoId;

    @NotNull(message = "ID do participante é obrigatório")
    private Long participanteId;
}
