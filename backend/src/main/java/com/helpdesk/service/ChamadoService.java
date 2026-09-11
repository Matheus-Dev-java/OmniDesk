package com.helpdesk.service;

import com.helpdesk.dto.ChamadoRequest;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.model.Chamado;
import com.helpdesk.model.Role;
import com.helpdesk.model.StatusChamado;
import com.helpdesk.model.Usuario;
import com.helpdesk.repository.ChamadoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public Chamado abrirChamado(ChamadoRequest dados, Usuario solicitante) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dados.getTitulo());
        chamado.setDescricao(dados.getDescricao());
        chamado.setCategoria(dados.getCategoria());
        chamado.setPrioridade(dados.getPrioridade());
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setSolicitante(solicitante);

        return chamadoRepository.save(chamado);
    }
    public List<Chamado> listarChamados(Usuario usuarioLogado) {
        if (usuarioLogado.getRole() == Role.ROLE_ADMIN) {
            return chamadoRepository.findAll();
        }
        return chamadoRepository.findBySolicitante(usuarioLogado);
    }
    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado nao encontrado com id " + id));
    }
    public Chamado atualizarStatus(Long id, StatusChamado novoStatus, Usuario usuarioLogado) {
        if (usuarioLogado.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Apenas administradores podem alterar o status de um chamado");
        }

        Chamado chamado = buscarPorId(id);
        chamado.setStatus(novoStatus);

        if (novoStatus == StatusChamado.FECHADO || novoStatus == StatusChamado.RESOLVIDO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }
        return chamadoRepository.save(chamado);
    }
}