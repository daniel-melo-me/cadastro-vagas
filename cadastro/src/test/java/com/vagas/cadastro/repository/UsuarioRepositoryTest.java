package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Perfil;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
        perfilRepository.deleteAll();
        arquivoRepository.deleteAll();
    }

    @Test
    public void deveCriarUmUsuario() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        perfilRepository.save(perfil);

        usuario.setPerfis(perfil);
        Arquivo imagem = new Arquivo();
        imagem.setFileName("Service_Virtualization_Vetor.png");
        imagem.setFileType("image/png");
        byte[] data = new byte[5];
        imagem.setData(data);
        arquivoRepository.save(imagem);
        usuario.setArquivo(imagem);
        repository.save(usuario);

        Assert.assertEquals("image/png", usuario.getArquivo().getFileType());
    }

    @Test
    public void deveDeletarUmUsuario() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        perfilRepository.save(perfil);

        usuario.setPerfis(perfil);
        Arquivo imagem = new Arquivo();
        imagem.setFileName("Service_Virtualization_Vetor.png");
        imagem.setFileType("image/png");
        byte[] data = new byte[5];
        imagem.setData(data);
        arquivoRepository.save(imagem);
        usuario.setArquivo(imagem);
        repository.save(usuario);
        repository.delete(usuario);

        Iterable<Usuario> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmUsuario() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        perfilRepository.save(perfil);

        usuario.setPerfis(perfil);
        Arquivo imagem = new Arquivo();
        imagem.setFileName("Service_Virtualization_Vetor.png");
        imagem.setFileType("image/png");
        byte[] data = new byte[5];
        imagem.setData(data);
        arquivoRepository.save(imagem);
        usuario.setArquivo(imagem);
        repository.save(usuario);

        usuario.setNome("editado");

        Assert.assertEquals("editado", usuario.getNome());
    }

    @Test
    public void deveRetornarUmaListaDeUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        usuario.setPerfis(perfil);

        perfilRepository.save(perfil);
        repository.save(usuario);

        Assert.assertNotNull(repository.findAll());
    }

    @Test
    public void deveFiltrarERetornarUmaListaDeUsuariosVerificandoEmailEMatricula() {
        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        usuario.setPerfis(perfil);

        perfilRepository.save(perfil);
        repository.save(usuario);

        Assert.assertNotNull(repository.findByMatriculaContainsOrNomeContainsOrEmailContains(
                usuario.getMatricula(),
                usuario.getNome(),
                usuario.getEmail(),
                null
        ));
        Assert.assertTrue(repository.existsByEmail(usuario.getEmail()));
        Assert.assertTrue(repository.existsByMatricula(usuario.getMatricula()));
    }
}
