package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.request.CurriculoRequestDTO;
import com.vagas.cadastro.service.CurriculoService;
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
@RequestMapping(path = "/curriculo")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CurriculoController {

    private final CurriculoService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/criar")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Validated CurriculoRequestDTO dto) {
        try {
            service.salvar(dto);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "Currículo deletado com sucesso!");
        try {
            service.deletar(id);
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
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody CurriculoRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    public ResponseEntity<?> listar(Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.listar(pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Validated CurriculoRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(service.editar(id, dto));
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
