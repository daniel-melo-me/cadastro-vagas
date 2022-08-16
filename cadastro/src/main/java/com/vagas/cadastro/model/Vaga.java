package com.vagas.cadastro.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vagas.cadastro.model.enumeration.InstitucionalEnum;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vaga")
@ToString
public class Vaga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    private String link;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private InstitucionalEnum institucional;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime expiracao;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private StatusEnum status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dataCriacao = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vagas_tags",
            joinColumns = @JoinColumn(name = "vaga_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tags> tags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "candidaturas",
            joinColumns = @JoinColumn(name = "vaga_id"), inverseJoinColumns = @JoinColumn(name = "curriculo_id"))
    private List<Curriculo> curriculos;

}
