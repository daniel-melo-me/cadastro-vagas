package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.LoginDTO;
import com.vagas.cadastro.dto.TokenDTO;
import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.service.TokenService;
import com.vagas.cadastro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AutenticacaoController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UsuarioService service;
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> autenticar(@RequestBody @Validated LoginDTO dto) {
        UsernamePasswordAuthenticationToken dadosLogin = dto.converter();
        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));

        } catch (AuthenticationException e) {
            return retornoErro(e.getMessage());
        }
    }

    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<?> registrar(@RequestBody @Validated UsuarioRequestDTO dto) {
        try {
            service.salvarUsuario(dto);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    private ResponseEntity<?> retornoErro(String mensagem) {
        erro.put("erro", mensagem);
        log.error("Erro encontrado: " + mensagem);
        return ResponseEntity.badRequest().body(erro);
    }
}
