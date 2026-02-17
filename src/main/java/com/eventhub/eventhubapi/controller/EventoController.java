package com.eventhub.eventhubapi.controller;

import com.eventhub.eventhubapi.dto.EventoRequest;
import com.eventhub.eventhubapi.dto.EventoResponse;
import com.eventhub.eventhubapi.exception.EventoNaoEncontradoException;
import com.eventhub.eventhubapi.model.Evento;
import com.eventhub.eventhubapi.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<EventoResponse>> listarTodos() {
        List<EventoResponse> responses = eventoService.listarTodos().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> buscarPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
        return ResponseEntity.ok(toResponse(evento));
    }

    @PostMapping
    public ResponseEntity<EventoResponse> criar(@RequestBody @Valid EventoRequest request) {
        Evento evento = toEntity(request);
        Evento salvo = eventoService.salvar(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EventoRequest request) {
        Evento evento = toEntity(request);
        Evento atualizado = eventoService.atualizar(id, evento)
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!eventoService.excluirPorId(id)) {
            throw new EventoNaoEncontradoException(id);
        }
        return ResponseEntity.noContent().build();
    }

    private Evento toEntity(EventoRequest request) {
        return Evento.builder()
                .nome(request.getNome())
                .data(request.getData())
                .local(request.getLocal())
                .capacidade(request.getCapacidade())
                .build();
    }

    private EventoResponse toResponse(Evento evento) {
        return EventoResponse.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .data(evento.getData())
                .local(evento.getLocal())
                .capacidade(evento.getCapacidade())
                .build();
    }
}
