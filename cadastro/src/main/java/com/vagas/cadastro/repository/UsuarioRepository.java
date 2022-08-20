package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByMatricula(String matricula);

    Page<Usuario> findByMatriculaContainsOrNomeContainsOrEmailContains(
            String matricula,
            String nome,
            String email,
            Pageable pageable);

    Optional<Usuario> findByMatricula(String matricula);

}
