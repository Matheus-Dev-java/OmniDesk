package com.helpdesk.service;

import com.helpdesk.dto.RegisterRequest;
import com.helpdesk.exception.RegraNegocioException;
import com.helpdesk.model.Role;
import com.helpdesk.model.Usuario;
import com.helpdesk.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Usuario registrar(RegisterRequest dados) {
        if (usuarioRepository.existsByEmail(dados.getEmail())) {
            throw new RegraNegocioException("Ja existe um usuario cadastrado com este email");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
        usuario.setRole(Role.ROLE_USER);

        return usuarioRepository.save(usuario);
    }
}