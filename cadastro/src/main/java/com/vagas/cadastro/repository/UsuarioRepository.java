package com.vagas.cadastro.repository;

import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByMatricula(String matricula);

//    @Query("SELECT u FROM Usuario u " +
//            " WHERE (:#{#filter.id} IS NULL OR :#{#filter.id} = u.id) " +
//            " AND (:#{#filter.nome} IS NULL OR :#{#filter.nome} = u.nome) " +
//            " AND (:#{#filter.email} IS NULL OR :#{#filter.email} = u.email)")
@Query(value = "SELECT u FROM Usuario u " +
        "   WHERE u.nome LIKE CONCAT('%',:#{#filter.nome},'%') " +
        "   AND u.email LIKE CONCAT('%',:#{#filter.email},'%') " +
        "   AND u.matricula LIKE CONCAT('%',:#{#filter.matricula},'%') "    )
    Page<Usuario> findFilterList(
            UsuarioRequestDTO filter,
            Pageable pageable);

    Optional<Usuario> findByMatricula(String matricula);
}
