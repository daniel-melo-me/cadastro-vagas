package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagsService {

    Tags salvar(TagsRequestDTO dto);

    void deletar(Long id);

    Tags pesquisar(Long id);

    Page<Tags> findPagedByFilters(TagsRequestDTO filter, Pageable pageable);

    Tags editar(Long id, Tags tags);
}
