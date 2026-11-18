package com.estudos.demo.repository;

import com.estudos.demo.entity.ProdutoEntity;
import com.estudos.demo.entity.CategoriaEntity;
import com.estudos.demo.dto.Produto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void deveConsultarSomenteProdutosDaCategoria() {
        CategoriaEntity perifericos = categoriaRepository.save(new CategoriaEntity(10L, "Perifericos"));
        CategoriaEntity outras = categoriaRepository.save(new CategoriaEntity(11L, "Outras"));
        repository.save(new ProdutoEntity(101L, "Teclado", 150.0, perifericos));
        repository.save(new ProdutoEntity(102L, "Monitor", 900.0, outras));
        repository.flush();
        entityManager.clear();

        var resultado = repository.findByCategoriaId(10L);

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
    }

    @Test
    void deveSalvarEBuscarProduto() {
        ProdutoEntity produto =
                new ProdutoEntity(100L, "Monitor", 900.0, null);

        repository.saveAndFlush(produto);

        entityManager.clear();

        var resultado = repository.findById(100L);

        assertTrue(resultado.isPresent());
        assertEquals("Monitor", resultado.get().getNome());
    }
    
}
