package com.vagas.cadastro.dto.request;

import com.sun.istack.NotNull;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.InstitucionalEnum;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class VagaRequestDTO {

    private Long id;
    @NotNull
    private String titulo;
    @NotNull
    private String descricao;
    private String link;
    private InstitucionalEnum institucional;
    private LocalDateTime expiracao;
    private StatusEnum status;
    private Usuario usuario;
    private List<Curriculo> curriculos;
    private Set<Tags> tags;

    public VagaRequestDTO() {
    }

    public VagaRequestDTO(Vaga vaga) {
        this.id = vaga.getId();
        this.titulo = vaga.getTitulo();
        this.descricao = vaga.getDescricao();
        this.link = vaga.getLink();
        this.institucional = vaga.getInstitucional();
        this.expiracao = vaga.getExpiracao();
        this.status = vaga.getStatus();
        this.usuario = vaga.getUsuario();
        this.curriculos = vaga.getCurriculos();
        this.tags = vaga.getTags();
    }

    public Vaga convert() {
        Vaga vaga = new Vaga();
        vaga.setId(this.id);
        vaga.setTitulo(this.titulo);
        vaga.setDescricao(this.descricao);
        vaga.setLink(this.link);
        vaga.setInstitucional(this.institucional);
        vaga.setExpiracao(this.expiracao);
        vaga.setStatus(this.status);
        vaga.setUsuario(this.usuario);
        vaga.setCurriculos(this.curriculos);
        vaga.setTags(this.tags);
        return vaga;
    }
}
