package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.dto.request.UsuarioEditRequestDTO;
import com.vagas.cadastro.dto.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.ArquivoRepository;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final ArquivoRepository arquivoRepository;
    private final PasswordEncoder encoder;
    private final ArrayList<String> matriculasProfessor = new ArrayList<>();
    private final ArrayList<String> extensaoImagens = new ArrayList<>();
    private static final String[] EXTENCOES = {
            ".JPEG", ".jpeg", ".JFIF", ".jfif", ".BMP", ".bpm", ".PNG", ".png", "webp",
            ".PSD", ".psd", ".TIFF", ".tif", "EXIF", "exif", "RAW", "raw", "WEBP",
    };

    @Override
    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Usuario editar(Long id, UsuarioEditRequestDTO dto) {
        verificarId(id);
        try {
            Usuario userMatricula = repository.getReferenceById(id);
            String matricula = userMatricula.getMatricula();
            if (repository.existsById(id)) {
                if (existsByEmail(dto.getEmail())) {
                    throw new RuntimeException("E-mail ja existente");
                }
                Usuario usuario = dto.convert();
                Usuario perfil = repository.findById(id).orElseThrow();
                usuario.setMatricula(matricula);
                usuario.setId(id);
                usuario.setPerfis(perfil.getPerfis());
                usuario.setSenha(encoder.encode(usuario.getSenha()));
                repository.save(usuario);
                return usuario;
            }
            throw new RuntimeException("erro desconhecido");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public Boolean existsByMatricula(String matricula) {
        return repository.existsByMatricula(matricula);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Usuario pesquisar(Long id) {
        if (repository.existsById(id)) {
            return repository.findById(id).orElseThrow();
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    @Override
    public Page<Usuario> findPagedByFilters(UsuarioRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }

        return repository.findByMatriculaContainsOrNomeContainsOrEmailContains(
                filter.getMatricula(),
                filter.getNome(),
                filter.getEmail(),
                pageable);
    }

    private void setarPerfil(Long id, UsuarioRequestDTO dto) {
        dto.getPerfil().setId(id);
    }

    @Override
    public void salvarUsuario(UsuarioRequestDTO dto) {
        validarImagem(dto);
        validarCampos(dto);
        Usuario usuario = dto.convert();
        usuario.setSenha(encoder.encode(dto.getSenha()));
        repository.save(usuario);
    }

    private void validarImagem(UsuarioRequestDTO dto) {
        carregaExtensaoImagens();
        if (!isNull(dto.getArquivo())) {
            Arquivo arquivo = arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow();
            int digito = arquivo.getFileName().lastIndexOf(".");
            String extencao = arquivo.getFileName().substring(arquivo.getFileName().length() - (arquivo.getFileName().length() - digito));
            if (!extensaoImagens.contains(extencao)) {
                throw new RuntimeException("Imagem com formato incorreto");
            }
        }
    }

    private void validarCampos(UsuarioRequestDTO dto) {
        validarNuloECampoEmBranco(dto.getNome(), "nome");
        validarNuloECampoEmBranco(dto.getEmail(), "email");
        validarNuloECampoEmBranco(dto.getSenha(), "senha");
        validarNuloECampoEmBranco(dto.getMatricula(), "matricula");
        validarInformacoes(dto);
    }

    private void validarNuloECampoEmBranco(String campo, String nome) {
        if (isNull(campo) || isBlank(campo)) {
            throw new RuntimeException("Campo " + nome + " obrigatório");
        }
    }

    private void validarInformacoes(UsuarioRequestDTO dto) {
        boolean aluno = dto.getPerfil().getNome().equals(PerfilEnum.ROLE_ALUNO);
        boolean professor = dto.getPerfil().getNome().equals(PerfilEnum.ROLE_PROFESSOR);

        validarCamposExistentes(dto);

        if (!isNull(dto.getArquivo())) {
            if (!arquivoRepository.existsById(dto.getArquivo().getId())) {
                throw new RuntimeException("Id do arquivo inválido");
            }
        }
        if (!isNull(dto.getPerfil().getId())) {
            throw new RuntimeException("Não passe o Id do perfil");
        }
        if (!aluno && !professor) {
            throw new RuntimeException("Nome de perfil incorreto");
        }
        if (aluno) {
            setarPerfil(2L, dto);
        }
        if (professor) {
            carregaMatriculas();
            if (matriculasProfessor.contains(dto.getMatricula())) {
                setarPerfil(3L, dto);
            } else {
                throw new RuntimeException("Não foi possivel criar o usuário");
            }
        }
    }

    private void validarCamposExistentes(UsuarioRequestDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail ja existente");
        }
        if (existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException("Matricula já existente");
        }
    }

    private void carregaExtensaoImagens() {
        extensaoImagens.addAll(List.of(EXTENCOES));
    }

    private void carregaMatriculas() {
        matriculasProfessor.add("10020030011");
        matriculasProfessor.add("10020030022");
        matriculasProfessor.add("10020030033");
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }
}
