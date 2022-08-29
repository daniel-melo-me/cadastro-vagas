package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VagaService {

    Page<Vaga> listar(Pageable pageable);

    Vaga salvar(VagaRequestDTO dto, Long idUser);

    void deletar(Long id);

    Vaga pesquisar(Long id);

    Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable);

    Vaga editar(Long id, VagaRequestDTO dto, Long idUser);

}
