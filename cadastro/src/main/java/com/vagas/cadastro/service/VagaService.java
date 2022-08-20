package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VagaService {

    Page<Vaga> listar(Pageable pageable);

    void salvar(VagaRequestDTO dto);

    void deletar(Long id);

    Vaga pesquisar(Long id);

    Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable);

    Vaga editar(Long id, VagaRequestDTO dto);

}
