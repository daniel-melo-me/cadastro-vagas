package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.request.UsuarioEditRequestDTO;
import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.TokenService;
import com.vagas.cadastro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/usuario")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UsuarioController {

    private final TokenService tokenService;
    private final UsuarioService service;
    private final Map<String, String> erro = new HashMap<>();
    private final Map<String, String> retorno = new HashMap<>();
    private final UsuarioRepository repository;

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid UsuarioEditRequestDTO dto) {
        try {
            if (verificacaoDePermissaoPeloId(id)) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok(service.editar(id, dto));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (verificacaoDePermissaoPeloId(id)) {
                return ResponseEntity.status(401).build();
            }
            service.deletar(id);
            retorno.put("response", "Usuário deletado com sucesso!");
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody @Valid UsuarioRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listar(Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.listar(pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    private ResponseEntity<?> retornoErro(String mensagem) {
        erro.put("erro", mensagem);
        log.error("Erro encontrado: " + mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    private boolean verificacaoDePermissaoPeloId(Long id) {
        Usuario usuario = repository.findById(tokenService.returnId()).orElseThrow();
        if (!usuario.getPerfis().getNome().equals(PerfilEnum.ROLE_ADMIN)) {
            return !Objects.equals(tokenService.returnId(), id);
        }
        return false;
    }
}
