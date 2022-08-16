package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface VagaService {

    Vaga salvar(Vaga vaga);

    void deletar(Long id);

    Vaga pesquisar(Long id);

    Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable);

    Vaga editar(Long id, Vaga vaga);

    ResponseEntity<?> validar(VagaRequestDTO dto);
}
