package com.eventhub.eventhubapi.exception;

public class EventoComIngressosException extends RuntimeException {

    public EventoComIngressosException(Long id) {
        super("Não é possível excluir evento com id " + id + ": existem ingressos vendidos para este evento.");
    }
}
