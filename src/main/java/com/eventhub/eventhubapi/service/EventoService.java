package com.eventhub.eventhubapi.service;

import com.eventhub.eventhubapi.exception.EventoComIngressosException;
import com.eventhub.eventhubapi.model.Evento;
import com.eventhub.eventhubapi.repository.EventoRepository;
import com.eventhub.eventhubapi.repository.IngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final IngressoRepository ingressoRepository;

    @Transactional(readOnly = true)
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    @Transactional
    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Transactional
    public Optional<Evento> atualizar(Long id, Evento eventoAtualizado) {
        return eventoRepository.findById(id)
                .map(existente -> {
                    existente.setNome(eventoAtualizado.getNome());
                    existente.setData(eventoAtualizado.getData());
                    existente.setLocal(eventoAtualizado.getLocal());
                    existente.setCapacidade(eventoAtualizado.getCapacidade());
                    return eventoRepository.save(existente);
                });
    }

    @Transactional
    public boolean excluirPorId(Long id) {
        if (!eventoRepository.existsById(id)) {
            return false;
        }
        if (ingressoRepository.existsByEvento_Id(id)) {
            throw new EventoComIngressosException(id);
        }
        eventoRepository.deleteById(id);
        return true;
    }
}
