package com.vagas.cadastro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDTO {

    private String token;
    private String bearer;

}
