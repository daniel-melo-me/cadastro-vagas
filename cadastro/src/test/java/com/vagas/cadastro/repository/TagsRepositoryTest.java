package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Tags;
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

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TagsRepositoryTest {

    @Autowired
    private TagsRepository repository;

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void deveCriarUmaTagEVerificarPeloId() {
        Tags tags = new Tags();
        tags.setNome("administração");
        repository.save(tags);

        Assert.assertTrue(repository.existsById(tags.getId()));
    }
}
