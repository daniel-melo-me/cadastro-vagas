package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final ArrayList<String> matriculasProfessor = new ArrayList<>();

    @Override
    public Usuario editar(Long id, Usuario usuario) {
        try {
            usuario.setId(id);
            usuario.setSenha(encoder.encode(usuario.getSenha()));
            repository.save(usuario);
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Boolean existsByMatricula(String matricula) {
        return repository.existsByMatricula(matricula);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Usuario pesquisar(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Page<Usuario> findPagedByFilters(UsuarioRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public ResponseEntity<?> validarCampos(UsuarioRequestDTO dto) {

        Map<String, String> emailExistente = new HashMap<>();
        Map<String, String> contaErro = new HashMap<>();
        Map<String, String> matriculaExistente = new HashMap<>();
        emailExistente.put("erro", "E-mail ja existente");
        contaErro.put("erro", "Nao foi possivel criar uma conta");
        matriculaExistente.put("erro", "Matricula ja existente");

        if (existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body(emailExistente);
        }

        if (existsByMatricula(dto.getMatricula())) {
            return ResponseEntity.badRequest().body(matriculaExistente);
        }

        if (!isNull(dto.getPerfil().getId())) {
            return ResponseEntity.badRequest().body(null);
        }

        if (dto.getPerfil().getNome().equals(PerfilEnum.ROLE_ADMIN)) {
            return ResponseEntity.status(403).body(contaErro);
        }

        if (dto.getPerfil().getNome().equals(PerfilEnum.ROLE_ALUNO)) {
            dto.getPerfil().setId(2L);
            return ResponseEntity.ok().body(salvarUsuario(dto));
        }

        if (dto.getPerfil().getNome().equals(PerfilEnum.ROLE_PROFESSOR)) {
            carregaMatriculas();
            if (matriculasProfessor.contains(dto.getMatricula())) {
                dto.getPerfil().setId(3L);
                return ResponseEntity.ok().body(salvarUsuario(dto));
            }
        }
        return ResponseEntity.badRequest().body(contaErro);
    }

    @Override
    public Usuario salvarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(
                dto.getPerfil(),
                dto.getNome(),
                dto.getEmail(),
                encoder.encode(dto.getSenha()),
                dto.getMatricula());
        return repository.save(usuario);
    }

    private void carregaMatriculas() {
        matriculasProfessor.add("10020030011");
        matriculasProfessor.add("10020030022");
        matriculasProfessor.add("10020030033");
    }
}
