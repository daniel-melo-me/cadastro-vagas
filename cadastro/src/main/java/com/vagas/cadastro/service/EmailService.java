package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.EmailRequestDTO;
import com.vagas.cadastro.model.Email;

public interface EmailService {

    Email sendEmail(EmailRequestDTO dto);

}
