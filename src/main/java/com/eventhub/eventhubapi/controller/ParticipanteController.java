package com.eventhub.eventhubapi.controller;

import com.eventhub.eventhubapi.dto.ParticipanteRequest;
import com.eventhub.eventhubapi.dto.ParticipanteResponse;
import com.eventhub.eventhubapi.exception.ParticipanteNaoEncontradoException;
import com.eventhub.eventhubapi.model.Participante;
import com.eventhub.eventhubapi.service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participantes")
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @GetMapping
    public ResponseEntity<List<ParticipanteResponse>> listarTodos() {
        List<ParticipanteResponse> responses = participanteService.listarTodos().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteResponse> buscarPorId(@PathVariable Long id) {
        Participante participante = participanteService.buscarPorId(id)
                .orElseThrow(() -> new ParticipanteNaoEncontradoException(id));
        return ResponseEntity.ok(toResponse(participante));
    }

    @PostMapping
    public ResponseEntity<ParticipanteResponse> criar(@RequestBody @Valid ParticipanteRequest request) {
        Participante participante = Participante.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .build();
        Participante salvo = participanteService.salvar(participante);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    private ParticipanteResponse toResponse(Participante participante) {
        return ParticipanteResponse.builder()
                .id(participante.getId())
                .nome(participante.getNome())
                .email(participante.getEmail())
                .build();
    }
}
