package com.eventhub.eventhubapi.service;

import com.eventhub.eventhubapi.dto.IngressoResponse;
import com.eventhub.eventhubapi.exception.EventoLotadoException;
import com.eventhub.eventhubapi.exception.EventoNaoEncontradoException;
import com.eventhub.eventhubapi.exception.ParticipanteNaoEncontradoException;
import com.eventhub.eventhubapi.model.Evento;
import com.eventhub.eventhubapi.model.Ingresso;
import com.eventhub.eventhubapi.model.Participante;
import com.eventhub.eventhubapi.repository.EventoRepository;
import com.eventhub.eventhubapi.repository.IngressoRepository;
import com.eventhub.eventhubapi.repository.ParticipanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompraIngressoService - validação de capacidade e regras de negócio")
class CompraIngressoServiceTest {

        @Mock
        private EventoRepository eventoRepository;

        @Mock
        private ParticipanteRepository participanteRepository;

        @Mock
        private IngressoRepository ingressoRepository;

        @InjectMocks
        private CompraIngressoService compraIngressoService;

        private static final Long EVENTO_ID = 1L;
        private static final Long PARTICIPANTE_ID = 1L;

        @Nested
        @DisplayName("realizarCompra - validações")
        class RealizarCompraValidacoes {

        @Test
        @DisplayName("deve lançar EventoNaoEncontradoException quando evento não existe")
        void deveLancarQuandoEventoNaoExiste() {
            when(eventoRepository.existsById(EVENTO_ID)).thenReturn(false);

            EventoNaoEncontradoException ex = assertThrows(EventoNaoEncontradoException.class,
                    () -> compraIngressoService.realizarCompra(EVENTO_ID, PARTICIPANTE_ID));

            assertEquals("Evento não encontrado com id: " + EVENTO_ID, ex.getMessage());
            verify(eventoRepository, never()).decrementarCapacidadeSeDisponivel(any());
            verify(ingressoRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar ParticipanteNaoEncontradoException quando participante não existe")
        void deveLancarQuandoParticipanteNaoExiste() {
            when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);
            when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(false);

            ParticipanteNaoEncontradoException ex = assertThrows(ParticipanteNaoEncontradoException.class,
                    () -> compraIngressoService.realizarCompra(EVENTO_ID, PARTICIPANTE_ID));

            assertEquals("Participante não encontrado com id: " + PARTICIPANTE_ID, ex.getMessage());
            verify(eventoRepository, never()).decrementarCapacidadeSeDisponivel(any());
            verify(ingressoRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar EventoLotadoException quando não há capacidade disponível")
        void deveLancarQuandoEventoLotado() {
            when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);
            when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
            when(eventoRepository.decrementarCapacidadeSeDisponivel(EVENTO_ID)).thenReturn(0);

            EventoLotadoException ex = assertThrows(EventoLotadoException.class,
                    () -> compraIngressoService.realizarCompra(EVENTO_ID, PARTICIPANTE_ID));

            assertEquals("Evento lotado. Não há capacidade disponível para o evento com id: " + EVENTO_ID, ex.getMessage());
            verify(ingressoRepository, never()).save(any());
        }
        }

        @Nested
        @DisplayName("realizarCompra - sucesso")
        class RealizarCompraSucesso {

                private Evento evento;
                private Participante participante;
                private Ingresso ingressoSalvo;

                @BeforeEach
                void setUp() {
                        evento = Evento.builder()
                                        .id(EVENTO_ID)
                                        .nome("Show")
                                        .data(LocalDateTime.now().plusDays(1))
                                        .local("Arena")
                                        .capacidade(100)
                                        .build();
                        participante = Participante.builder()
                                        .id(PARTICIPANTE_ID)
                                        .nome("João")
                                        .email("joao@email.com")
                                        .build();
                        ingressoSalvo = Ingresso.builder()
                                        .id(1L)
                                        .evento(evento)
                                        .participante(participante)
                                        .build();
                }

        @Test
        @DisplayName("deve confirmar compra e decrementar capacidade quando há vaga")
        void deveConfirmarCompraEDecrementarCapacidade() {
            when(eventoRepository.existsById(EVENTO_ID)).thenReturn(true);
            when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
            when(eventoRepository.decrementarCapacidadeSeDisponivel(EVENTO_ID)).thenReturn(1);
            when(eventoRepository.getReferenceById(EVENTO_ID)).thenReturn(evento);
            when(participanteRepository.getReferenceById(PARTICIPANTE_ID)).thenReturn(participante);
            when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingressoSalvo);

            var response = compraIngressoService.realizarCompra(EVENTO_ID, PARTICIPANTE_ID);

            assertEquals(ingressoSalvo.getId(), response.getId());
            assertEquals(EVENTO_ID, response.getEventoId());
            assertEquals(PARTICIPANTE_ID, response.getParticipanteId());
            assertEquals("Show", response.getNomeEvento());
            assertEquals("João", response.getNomeParticipante());
            verify(eventoRepository).decrementarCapacidadeSeDisponivel(EVENTO_ID);
            verify(ingressoRepository).save(any(Ingresso.class));
        }
        }

        @Nested
        @DisplayName("listarIngressosPorParticipante")
        class ListarIngressosPorParticipante {

        @Test
        @DisplayName("deve lançar ParticipanteNaoEncontradoException quando participante não existe")
        void deveLancarQuandoParticipanteNaoExiste() {
            when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(false);

            assertThrows(ParticipanteNaoEncontradoException.class,
                    () -> compraIngressoService.listarIngressosPorParticipante(PARTICIPANTE_ID));

            verify(ingressoRepository, never()).findByParticipanteOrderByDataCompraDesc(any());
        }

                @Test
                @DisplayName("deve retornar lista de ingressos quando participante existe")
                void deveRetornarListaQuandoParticipanteExiste() {
                        Participante participante = Participante.builder()
                                        .id(PARTICIPANTE_ID).nome("João").email("joao@email.com").build();
                        Evento evento = Evento.builder()
                                        .id(EVENTO_ID).nome("Show").data(LocalDateTime.now().plusDays(1))
                                        .local("Arena").capacidade(100).build();
                        Ingresso ingresso = Ingresso.builder()
                                        .id(1L).evento(evento).participante(participante).build();

                        when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
                        when(participanteRepository.getReferenceById(PARTICIPANTE_ID)).thenReturn(participante);
                        when(ingressoRepository.findByParticipanteOrderByDataCompraDesc(participante))
                                        .thenReturn(List.of(ingresso));

                        List<IngressoResponse> resultado = compraIngressoService
                                        .listarIngressosPorParticipante(PARTICIPANTE_ID);

                        assertNotNull(resultado);
                        assertEquals(1, resultado.size());
                        assertEquals(EVENTO_ID, resultado.get(0).getEventoId());
                        assertEquals("Show", resultado.get(0).getNomeEvento());
                        assertEquals("João", resultado.get(0).getNomeParticipante());
                        verify(ingressoRepository).findByParticipanteOrderByDataCompraDesc(participante);
                }

                @Test
                @DisplayName("deve retornar lista vazia quando participante não tem ingressos")
                void deveRetornarListaVaziaQuandoSemIngressos() {
                        Participante participante = Participante.builder()
                                        .id(PARTICIPANTE_ID).nome("João").email("joao@email.com").build();

                        when(participanteRepository.existsById(PARTICIPANTE_ID)).thenReturn(true);
                        when(participanteRepository.getReferenceById(PARTICIPANTE_ID)).thenReturn(participante);
                        when(ingressoRepository.findByParticipanteOrderByDataCompraDesc(participante))
                                        .thenReturn(Collections.emptyList());

                        List<IngressoResponse> resultado = compraIngressoService
                                        .listarIngressosPorParticipante(PARTICIPANTE_ID);

                        assertNotNull(resultado);
                        assertEquals(0, resultado.size());
                }
        }
}
