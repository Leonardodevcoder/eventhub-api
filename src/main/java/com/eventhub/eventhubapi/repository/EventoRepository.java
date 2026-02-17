package com.eventhub.eventhubapi.repository;

import com.eventhub.eventhubapi.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Evento e SET e.capacidade = e.capacidade - 1 WHERE e.id = :id AND e.capacidade > 0")
    int decrementarCapacidadeSeDisponivel(@Param("id") Long id);
}
