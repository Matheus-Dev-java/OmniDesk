package com.helpdesk.controller;

import com.helpdesk.dto.LoginRequest;
import com.helpdesk.dto.LoginResponse;
import com.helpdesk.dto.RegisterRequest;
import com.helpdesk.model.Usuario;
import com.helpdesk.service.AuthService;
import com.helpdesk.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacao")
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;
    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }
    @PostMapping("/registrar")
    @Operation(summary = "Cadastra um novo usuario com perfil ROLE_USER")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegisterRequest dados) {
        usuarioService.registrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/login")
    @Operation(summary = "Autentica um usuario e retorna o token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest dados) {
        LoginResponse resposta = authService.autenticar(dados);
        return ResponseEntity.ok(resposta);
    }
}