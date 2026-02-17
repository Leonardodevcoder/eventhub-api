package com.eventhub.eventhubapi.exception;

public class ParticipanteNaoEncontradoException extends RuntimeException {

    public ParticipanteNaoEncontradoException(Long id) {
        super("Participante não encontrado com id: " + id);
    }
}
