package com.vagas.cadastro.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vagas.cadastro.model.*;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioRequestDTO {

    private Long id;
    private Perfil perfil;
    private List<Curriculo> curriculos;
    private Arquivo imagem;
    private String nome;
    private String email;
    private String senha;
    private String matricula;
    @JsonIgnore
    private List<Vaga> vagas;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.perfil = usuario.getPerfis();
        this.curriculos = usuario.getCurriculos();
        this.imagem = usuario.getArquivo();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.matricula = usuario.getMatricula();
        this.vagas = usuario.getVagas();
    }

    public Usuario convert() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setPerfis(this.perfil);
        usuario.setCurriculos(this.curriculos);
        usuario.setArquivo(this.imagem);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setMatricula(this.matricula);
        usuario.setVagas(this.vagas);
        return usuario;
    }
}
