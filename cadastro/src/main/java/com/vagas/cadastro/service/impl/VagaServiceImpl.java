package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.VagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VagaServiceImpl implements VagaService {

    private final VagaRepository repository;

    @Override
    public Vaga salvar(Vaga vaga) {
        return repository.save(vaga);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Vaga pesquisar(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public Vaga editar(Long id, Vaga vaga) {
        vaga.setId(id);
        return repository.save(vaga);
    }

    @Override
    public ResponseEntity<?> validar(VagaRequestDTO dto) {
        if (dto.getTitulo().isBlank() || isNull(dto.getTitulo())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (dto.getDescricao().isBlank() || isNull(dto.getDescricao())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Vaga vaga = dto.convert();
            return ResponseEntity.ok().body(salvar(vaga));
        }
    }
}
