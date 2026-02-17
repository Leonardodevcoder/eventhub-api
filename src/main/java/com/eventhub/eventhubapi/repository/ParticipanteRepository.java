package com.eventhub.eventhubapi.repository;

import com.eventhub.eventhubapi.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    boolean existsByEmail(String email);
}
