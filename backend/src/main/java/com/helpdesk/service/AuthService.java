package com.helpdesk.service;

import com.helpdesk.dto.LoginRequest;
import com.helpdesk.dto.LoginResponse;
import com.helpdesk.model.Usuario;
import com.helpdesk.repository.UsuarioRepository;
import com.helpdesk.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }
    public LoginResponse autenticar(LoginRequest dados) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));

        String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getRole().name());

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail(), usuario.getRole().name());
    }
}