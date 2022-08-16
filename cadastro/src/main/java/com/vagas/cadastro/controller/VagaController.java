package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.service.VagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/vaga")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class VagaController {

    private final VagaService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/criar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Validated VagaRequestDTO dto) {
        try {
            return service.validar(dto);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "vaga deletada com sucesso!");
        try {
            service.deletar(id);
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody VagaRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Validated VagaRequestDTO dto) {
        try {
            Vaga vaga = dto.convert();
            return ResponseEntity.ok().body(service.editar(id, vaga));
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
