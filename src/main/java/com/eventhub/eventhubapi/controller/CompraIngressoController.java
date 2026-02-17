package com.eventhub.eventhubapi.controller;

import com.eventhub.eventhubapi.dto.CompraIngressoRequest;
import com.eventhub.eventhubapi.dto.IngressoResponse;
import com.eventhub.eventhubapi.service.CompraIngressoService;
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

@RestController
@RequestMapping("/api/ingressos")
@RequiredArgsConstructor
public class CompraIngressoController {

    private final CompraIngressoService compraIngressoService;

    @PostMapping("/comprar")
    public ResponseEntity<IngressoResponse> comprar(@RequestBody @Valid CompraIngressoRequest request) {
        IngressoResponse ingresso = compraIngressoService.realizarCompra(request.getEventoId(),
                request.getParticipanteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresso);
    }

    @GetMapping("/participante/{participanteId}")
    public ResponseEntity<List<IngressoResponse>> listarPorParticipante(@PathVariable Long participanteId) {
        List<IngressoResponse> ingressos = compraIngressoService.listarIngressosPorParticipante(participanteId);
        return ResponseEntity.ok(ingressos);
    }
}
