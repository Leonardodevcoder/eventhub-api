package com.eventhub.eventhubapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratamentoExcecoesHandler {

    @ExceptionHandler(EventoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> eventoNaoEncontrado(EventoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(EventoLotadoException.class)
    public ResponseEntity<Map<String, Object>> eventoLotado(EventoLotadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(ParticipanteNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> participanteNaoEncontrado(ParticipanteNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(EventoComIngressosException.class)
    public ResponseEntity<Map<String, Object>> eventoComIngressos(EventoComIngressosException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> emailJaCadastrado(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> violacaoIntegridade(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensagem", "Dados inválidos ou já existentes. Verifique os dados enviados."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacaoFalhou(MethodArgumentNotValidException ex) {
        String erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", "Erro de validação");
        body.put("erros", erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", "Corpo da requisição inválido ou JSON malformado."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", "Parâmetro inválido. Verifique o ID ou os valores enviados na URL."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> parametroObrigatorio(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", "Parâmetro obrigatório ausente: " + ex.getParameterName()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> metodoNaoPermitido(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of("mensagem", "Método não permitido para esta URL. Use o método HTTP correto."));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> recursoNaoEncontrado(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", "Recurso não encontrado: " + ex.getRequestURL()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> argumentoInvalido(IllegalArgumentException ex) {
        String mensagem = ex.getMessage() != null && !ex.getMessage().isEmpty()
                ? ex.getMessage()
                : "Argumento ou parâmetro inválido.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> erroInesperado(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensagem", "Ocorreu um erro interno. Tente novamente mais tarde."));
    }
}
