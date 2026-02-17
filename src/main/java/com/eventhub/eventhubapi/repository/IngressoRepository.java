package com.eventhub.eventhubapi.repository;

import com.eventhub.eventhubapi.model.Ingresso;
import com.eventhub.eventhubapi.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    List<Ingresso> findByParticipanteOrderByDataCompraDesc(Participante participante);

    boolean existsByEvento_Id(Long eventoId);
}
