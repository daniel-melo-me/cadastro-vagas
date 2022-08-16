package com.vagas.cadastro.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class LoginDTO {

    private String matricula;
    private String senha;

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(matricula, senha);
    }
}
