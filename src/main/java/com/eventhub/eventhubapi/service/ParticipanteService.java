package com.eventhub.eventhubapi.service;

import com.eventhub.eventhubapi.exception.EmailJaCadastradoException;
import com.eventhub.eventhubapi.model.Participante;
import com.eventhub.eventhubapi.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    @Transactional(readOnly = true)
    public List<Participante> listarTodos() {
        return participanteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Participante> buscarPorId(Long id) {
        return participanteRepository.findById(id);
    }

    @Transactional
    public Participante salvar(Participante participante) {
        String email = participante.getEmail() != null ? participante.getEmail().trim() : "";
        participante.setEmail(email);
        if (participanteRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(email);
        }
        return participanteRepository.save(participante);
    }
}
