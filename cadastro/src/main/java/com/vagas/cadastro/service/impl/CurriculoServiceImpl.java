package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.repository.ArquivoRepository;
import com.vagas.cadastro.repository.CurriculoRepository;
import com.vagas.cadastro.service.CurriculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurriculoServiceImpl implements CurriculoService {

    private final CurriculoRepository repository;
    private final ArquivoRepository arquivoRepository;
    private final ArrayList<String> extensaoImagens = new ArrayList<>();
    private final Map<String, String> erro = new HashMap<>();
    private static final String[] EXTENCOES = {
            ".TXT", ".txt", ".PDF", ".pdf", ".DOC", ".doc", ".DOCX", ".docx",
            ".ppt", ".PPT", ".pps", ".PPS"
    };

    @Override
    public Curriculo salvar(CurriculoRequestDTO dto) {
        Curriculo curriculo = dto.convert();
        return repository.save(curriculo);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Curriculo pesquisar(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Page<Curriculo> findPagedByFilters(CurriculoRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public Curriculo editar(Long id, Curriculo curriculo) {
        curriculo.setId(id);
        return repository.save(curriculo);
    }

    @Override
    public ResponseEntity<?> validaExtencaoImagem(CurriculoRequestDTO dto) {
        carregaExtensaoImagens();

        Arquivo arquivo = arquivoRepository.findById(dto.getArquivo().getId()).orElse(null);

        assert arquivo != null;
        int digito = arquivo.getFileName().lastIndexOf(".");
        String extencao = arquivo.getFileName().substring(arquivo.getFileName().length() - (arquivo.getFileName().length() - digito));

        if (!extensaoImagens.contains(extencao)) {
            return retornoErro();
        }

        return ResponseEntity.ok().body(salvar(dto));
    }

    public void carregaExtensaoImagens() {
        extensaoImagens.addAll(List.of(EXTENCOES));
    }

    private ResponseEntity<?> retornoErro() {
        erro.put("erro", "formato de documento inválido");
        return ResponseEntity.badRequest().body(erro);
    }
}
