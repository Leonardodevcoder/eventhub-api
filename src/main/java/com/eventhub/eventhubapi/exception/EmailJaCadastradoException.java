package com.eventhub.eventhubapi.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Já existe um participante cadastrado com o e-mail: " + email);
    }
}
