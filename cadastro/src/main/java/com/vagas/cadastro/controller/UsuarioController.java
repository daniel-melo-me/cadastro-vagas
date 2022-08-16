package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/usuario")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UsuarioController {

    private final UsuarioService service;
    private final Map<String, String> erro = new HashMap<>();
    private final Map<String, String> retorno = new HashMap<>();

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Validated UsuarioRequestDTO dto) {
        try {
            Usuario usuario = dto.convert();
            return ResponseEntity.ok(service.editar(id, usuario));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            retorno.put("response", "Usuario deletado com sucesso!");
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody UsuarioRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
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
