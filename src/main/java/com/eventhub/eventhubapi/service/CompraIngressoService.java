package com.eventhub.eventhubapi.service;

import com.eventhub.eventhubapi.dto.IngressoResponse;
import com.eventhub.eventhubapi.exception.EventoLotadoException;
import com.eventhub.eventhubapi.exception.EventoNaoEncontradoException;
import com.eventhub.eventhubapi.exception.ParticipanteNaoEncontradoException;
import com.eventhub.eventhubapi.model.Ingresso;
import com.eventhub.eventhubapi.repository.EventoRepository;
import com.eventhub.eventhubapi.repository.IngressoRepository;
import com.eventhub.eventhubapi.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraIngressoService {

    private final EventoRepository eventoRepository;
    private final ParticipanteRepository participanteRepository;
    private final IngressoRepository ingressoRepository;

    @Transactional
    public IngressoResponse realizarCompra(Long eventoId, Long participanteId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new EventoNaoEncontradoException(eventoId);
        }
        if (!participanteRepository.existsById(participanteId)) {
            throw new ParticipanteNaoEncontradoException(participanteId);
        }

        int atualizado = eventoRepository.decrementarCapacidadeSeDisponivel(eventoId);
        if (atualizado == 0) {
            throw new EventoLotadoException(eventoId);
        }

        Ingresso ingresso = Ingresso.builder()
                .evento(eventoRepository.getReferenceById(eventoId))
                .participante(participanteRepository.getReferenceById(participanteId))
                .build();
        ingresso = ingressoRepository.save(ingresso);
        return toResponse(ingresso);
    }

    @Transactional(readOnly = true)
    public List<IngressoResponse> listarIngressosPorParticipante(Long participanteId) {
        if (!participanteRepository.existsById(participanteId)) {
            throw new ParticipanteNaoEncontradoException(participanteId);
        }
        List<Ingresso> ingressos = ingressoRepository.findByParticipanteOrderByDataCompraDesc(
                participanteRepository.getReferenceById(participanteId));
        return ingressos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private IngressoResponse toResponse(Ingresso ingresso) {
        return IngressoResponse.builder()
                .id(ingresso.getId())
                .eventoId(ingresso.getEvento().getId())
                .nomeEvento(ingresso.getEvento().getNome())
                .participanteId(ingresso.getParticipante().getId())
                .nomeParticipante(ingresso.getParticipante().getNome())
                .dataCompra(ingresso.getDataCompra())
                .build();
    }
}
