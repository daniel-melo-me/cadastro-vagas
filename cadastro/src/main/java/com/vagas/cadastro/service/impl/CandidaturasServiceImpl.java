package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.CandidaturasRequestDTO;
import com.vagas.cadastro.model.Candidaturas;
import com.vagas.cadastro.repository.CandidaturasRepository;
import com.vagas.cadastro.repository.CurriculoRepository;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.CandidaturasService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CandidaturasServiceImpl implements CandidaturasService {

    private final CandidaturasRepository repository;
    private final VagaRepository vagaRepository;
    private final CurriculoRepository curriculoRepository;

    @Override
    public Page<Candidaturas> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Candidaturas salvar(CandidaturasRequestDTO dto) {
        validarCampos(dto);
        Candidaturas candidaturas = dto.convert();
        validarInformacoes(candidaturas);
        return repository.save(candidaturas);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Candidaturas pesquisar(Long id) {
        if (repository.existsById(id)) {
            return repository.findById(id).orElse(null);
        }
        throw new RuntimeException("Candidatura não encontrada");
    }

    @Override
    public Candidaturas editar(Long id, CandidaturasRequestDTO dto) {
        verificarId(id);
        validarCampos(dto);
        Candidaturas candidaturas = dto.convert();
        validarInformacoes(candidaturas);
        candidaturas.setId(id);
        return repository.save(candidaturas);
    }

    private void validarCampos(CandidaturasRequestDTO dto) {
        validarObjetoNulo(dto.getCurriculo(), "curriculo");
        validarObjetoNulo(dto.getVaga(), "vaga");
    }

    private void validarObjetoNulo(Object objeto, String campo) {
        if (isNull(objeto)) {
            throw new RuntimeException("campo " + campo + " é obrigatório");
        }
    }

    private void validarInformacoes(Candidaturas candidaturas) {
        if (!vagaRepository.existsById(candidaturas.getVaga().getId())) {
            throw new RuntimeException("vaga não existente");
        }
        if (!curriculoRepository.existsById(candidaturas.getCurriculo().getId())) {
            throw new RuntimeException("curriculo não existente");
        }
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }
}
