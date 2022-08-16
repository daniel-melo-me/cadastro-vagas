package com.vagas.cadastro.model.enumeration;

public enum StatusEnum {
    FINALIZADO("Finalizado"),
    ABERTO("Aberto");

    private final String descricao;

    StatusEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
