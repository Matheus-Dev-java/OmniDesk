package com.helpdesk.controller;

import com.helpdesk.dto.ChamadoRequest;
import com.helpdesk.dto.ChamadoResponse;
import com.helpdesk.dto.StatusUpdateRequest;
import com.helpdesk.model.Chamado;
import com.helpdesk.model.Usuario;
import com.helpdesk.repository.UsuarioRepository;
import com.helpdesk.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chamados")
@Tag(name = "Chamados")
@SecurityRequirement(name = "bearerAuth")
public class ChamadoController {
    private final ChamadoService chamadoService;
    private final UsuarioRepository usuarioRepository;
    public ChamadoController(ChamadoService chamadoService, UsuarioRepository usuarioRepository) {
        this.chamadoService = chamadoService;
        this.usuarioRepository = usuarioRepository;
    }
    private Usuario usuarioLogado(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
    }
    @PostMapping
    @Operation(summary = "Abre um novo chamado tecnico")
    public ResponseEntity<ChamadoResponse> abrirChamado(@Valid @RequestBody ChamadoRequest dados, Authentication authentication) {
        Usuario solicitante = usuarioLogado(authentication);
        Chamado chamado = chamadoService.abrirChamado(dados, solicitante);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ChamadoResponse(chamado));
    }
    @GetMapping
    @Operation(summary = "Lista os chamados do usuario logado, ou todos se for administrador")
    public ResponseEntity<List<ChamadoResponse>> listarChamados(Authentication authentication) {
        Usuario usuarioLogado = usuarioLogado(authentication);
        List<ChamadoResponse> chamados = chamadoService.listarChamados(usuarioLogado).stream()
                .map(ChamadoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chamados);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Busca um chamado pelo id")
    public ResponseEntity<ChamadoResponse> buscarPorId(@PathVariable Long id) {
        Chamado chamado = chamadoService.buscarPorId(id);
        return ResponseEntity.ok(new ChamadoResponse(chamado));
    }
    @PutMapping("/{id}/status")
    @Operation(summary = "Altera o status de um chamado, exclusivo para administradores")
    public ResponseEntity<ChamadoResponse> atualizarStatus(@PathVariable Long id,
                                                           @Valid @RequestBody StatusUpdateRequest dados, Authentication authentication) {
        Usuario usuarioLogado = usuarioLogado(authentication);
        Chamado chamado = chamadoService.atualizarStatus(id, dados.getStatus(), usuarioLogado);
        return ResponseEntity.ok(new ChamadoResponse(chamado));
    }
}