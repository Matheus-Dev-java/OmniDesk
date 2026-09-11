package com.helpdesk.service;

import com.helpdesk.dto.ChamadoRequest;
import com.helpdesk.model.Chamado;
import com.helpdesk.model.PrioridadeChamado;
import com.helpdesk.model.Role;
import com.helpdesk.model.StatusChamado;
import com.helpdesk.model.Usuario;
import com.helpdesk.repository.ChamadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    private Usuario usuarioComum;
    private Usuario usuarioAdmin;

    @BeforeEach
    void configurar() {
        usuarioComum = new Usuario("Joao Silva", "joao.silva@empresa.com", "senha123", Role.ROLE_USER);
        usuarioComum.setId(1L);

        usuarioAdmin = new Usuario("Maria Suporte", "maria.suporte@empresa.com", "senha123", Role.ROLE_ADMIN);
        usuarioAdmin.setId(2L);
    }

    @Test
    void deveAbrirChamadoComDadosCorretos() {
        ChamadoRequest request = new ChamadoRequest();
        request.setTitulo("Troca de monitor");
        request.setDescricao("Monitor apresentando falha de imagem intermitente");
        request.setCategoria("Hardware");
        request.setPrioridade(PrioridadeChamado.MEDIA);

        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Chamado chamadoCriado = chamadoService.abrirChamado(request, usuarioComum);

        ArgumentCaptor<Chamado> captor = ArgumentCaptor.forClass(Chamado.class);
        verify(chamadoRepository).save(captor.capture());

        Chamado chamadoSalvo = captor.getValue();
        assertEquals("Troca de monitor", chamadoSalvo.getTitulo());
        assertEquals(StatusChamado.ABERTO, chamadoSalvo.getStatus());
        assertEquals(usuarioComum, chamadoSalvo.getSolicitante());
        assertEquals(StatusChamado.ABERTO, chamadoCriado.getStatus());
    }

    @Test
    void devePermitirAdminAlterarStatusDoChamado() {
        Chamado chamado = new Chamado();
        chamado.setId(10L);
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setSolicitante(usuarioComum);

        when(chamadoRepository.findById(10L)).thenReturn(Optional.of(chamado));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Chamado chamadoAtualizado = chamadoService.atualizarStatus(10L, StatusChamado.EM_ANDAMENTO, usuarioAdmin);

        assertEquals(StatusChamado.EM_ANDAMENTO, chamadoAtualizado.getStatus());
    }

    @Test
    void naoDevePermitirUsuarioComumAlterarStatusDoChamado() {
        assertThrows(AccessDeniedException.class, () ->
                chamadoService.atualizarStatus(10L, StatusChamado.EM_ANDAMENTO, usuarioComum)
        );
    }
}