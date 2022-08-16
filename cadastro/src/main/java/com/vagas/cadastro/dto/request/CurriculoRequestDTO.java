package com.vagas.cadastro.dto.request;

import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class CurriculoRequestDTO {

    private Long id;
    private String descricao;
    private Arquivo arquivo;
    private Usuario usuario;
    private List<Tags> tags;

    public CurriculoRequestDTO() {
    }

    public CurriculoRequestDTO(Curriculo curriculo) {
        this.id = curriculo.getId();
        this.descricao = curriculo.getDescricao();
        this.arquivo = curriculo.getArquivo();
        this.usuario = curriculo.getUsuario();
        this.tags = curriculo.getTags();
    }

    public Curriculo convert() {
        Curriculo curriculo = new Curriculo();
        curriculo.setId(this.id);
        curriculo.setDescricao(this.descricao);
        curriculo.setArquivo(this.arquivo);
        curriculo.setUsuario(this.usuario);
        curriculo.setTags(this.tags);
        return curriculo;
    }
}
