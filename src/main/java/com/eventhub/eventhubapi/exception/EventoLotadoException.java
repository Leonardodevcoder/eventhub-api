package com.eventhub.eventhubapi.exception;

public class EventoLotadoException extends RuntimeException {

    public EventoLotadoException(Long eventoId) {
        super("Evento lotado. Não há capacidade disponível para o evento com id: " + eventoId);
    }
}
