package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.service.TagsService;
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
@RequestMapping("/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TagsController {

    private final TagsService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/criar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    public ResponseEntity<?> criar(@RequestBody @Validated TagsRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(service.salvar(dto));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "tag deletada com sucesso!");
        try {
            service.deletar(id);
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody TagsRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Validated TagsRequestDTO dto) {
        try {
            Tags tags = dto.convert();
            return ResponseEntity.ok().body(service.editar(id, tags));
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
