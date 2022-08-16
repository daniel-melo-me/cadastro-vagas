package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Curriculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CurriculoService {

    Curriculo salvar(CurriculoRequestDTO dto);

    void deletar(Long id);

    Curriculo pesquisar(Long id);

    Page<Curriculo> findPagedByFilters(CurriculoRequestDTO filter, Pageable pageable);

    Curriculo editar(Long id, Curriculo curriculo);

    ResponseEntity<?> validaExtencaoImagem(CurriculoRequestDTO dto);
}
