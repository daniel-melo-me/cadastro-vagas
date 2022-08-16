package com.vagas.cadastro.service;

import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    Usuario editar(Long id, Usuario usuario);

    Boolean existsByEmail(String email);

    Boolean existsByMatricula(String matricula);

    Usuario salvarUsuario(UsuarioRequestDTO dto);

    void deletar(Long id);

    Usuario pesquisar(Long id);

    Page<Usuario> findPagedByFilters(UsuarioRequestDTO filter, Pageable pageable);

    ResponseEntity<?> validarCampos(UsuarioRequestDTO dto);
}
