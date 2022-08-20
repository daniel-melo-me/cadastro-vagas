package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import com.vagas.cadastro.repository.TagsRepository;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.VagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class VagaServiceImpl implements VagaService {

    private final VagaRepository repository;
    private final TagsRepository tagsRepository;

    @Override
    public Page<Vaga> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void salvar(VagaRequestDTO dto) {
        validar(dto);
        validarData(dto.getExpiracao());
        Vaga vaga = dto.convert();
        validarTag(vaga);
        vaga.setStatus(StatusEnum.ABERTO);
        repository.save(vaga);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Vaga pesquisar(Long id) {
        if (repository.existsById(id)) {
            return repository.findById(id).orElseThrow();
        }
        throw new RuntimeException("Vaga não encontrada");
    }

    @Override
    public Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findByTituloContainsOrDescricaoContains(
                filter.getTitulo(),
                filter.getDescricao(),
                pageable
        );
    }

    @Override
    public Vaga editar(Long id, VagaRequestDTO dto) {
        verificarId(id);
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new RuntimeException("Título já existente");
        }
        validarData(dto.getExpiracao());
        Vaga vaga = dto.convert();
        validarTag(vaga);
        vaga.setId(id);
        return repository.save(vaga);
    }

    public void validar(VagaRequestDTO dto) {
        validarCampoNuloECampoEmBranco(dto.getTitulo(), "titulo");
        validarCampoNuloECampoEmBranco(dto.getDescricao(), "descricao");
        if (isNull(dto.getInstitucional())) {
            throw new RuntimeException("Campo institucional obrigatório");
        }
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new RuntimeException("Título já existente");
        }
    }

    private void validarCampoNuloECampoEmBranco(String campo, String nome) {
        if (isNull(campo) || isBlank(campo)) {
            throw new RuntimeException("Campo " + nome + " obrigatório");
        }
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }

    private void validarData(LocalDateTime data) {
        LocalDateTime dataAtual = LocalDateTime.now();
        if (!data.isAfter(dataAtual)) {
            throw new RuntimeException("Data de expiração já passou");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime.parse(data.format(formatter), formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválida");
        }
    }

    private void validarTag(Vaga vaga) {
        if (!isNull(vaga.getTags())) {
            for (Tags tags : vaga.getTags()) {
                if (!isNull(tags.getId())) {
                    if (!tagsRepository.existsById(tags.getId())) {
                        throw new RuntimeException("Tag não existente");
                    }
                }
            }
        }
    }
}
