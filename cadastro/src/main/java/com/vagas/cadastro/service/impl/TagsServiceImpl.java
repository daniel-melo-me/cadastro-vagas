package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.repository.TagsRepository;
import com.vagas.cadastro.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagsRepository repository;

    @Override
    public Tags salvar(TagsRequestDTO dto) {
        Tags tags = dto.convert();
        return repository.save(tags);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Tags pesquisar(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Page<Tags> findPagedByFilters(TagsRequestDTO filter, Pageable pageable) {
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public Tags editar(Long id, Tags tags) {
        tags.setId(id);
        return repository.save(tags);
    }
}
