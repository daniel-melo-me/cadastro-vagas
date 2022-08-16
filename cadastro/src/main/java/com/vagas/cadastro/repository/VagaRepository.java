package com.vagas.cadastro.repository;

import com.vagas.cadastro.dto.request.VagaRequestDTO;
import com.vagas.cadastro.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

//    @Query(value = "SELECT v FROM Vaga v " +
//            " WHERE (:#{#filter.id} IS NULL OR :#{#filter.id} = v.id) " +
//            " AND (:#{#filter.descricao} IS NULL OR :#{#filter.descricao} = v.descricao) " +
//            " AND (:#{#filter.expiracao} IS NULL OR :#{#filter.expiracao} = v.expiracao) " +
//            " AND (:#{#filter.titulo} IS NULL OR :#{#filter.titulo} LIKE '%:#{#v.titulo}%' )")
//    Page<Vaga> findFilterList(
//            VagaRequestDTO filter,
//            Pageable pageable);

    @Query(value = "SELECT v FROM Vaga v " +
            "   WHERE v.titulo LIKE CONCAT('%',:#{#filter.titulo},'%') " +
            "   AND v.descricao LIKE CONCAT('%',:#{#filter.descricao},'%') ")
    Page<Vaga> findFilterList(VagaRequestDTO filter, Pageable pageable);

}

