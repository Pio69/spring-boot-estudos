package com.estudos.demo.service;

import com.estudos.demo.repository.ProdutoRepository;
import com.estudos.demo.repository.CategoriaRepository;
import com.estudos.demo.entity.CategoriaEntity;
import com.estudos.demo.entity.ProdutoEntity;
import com.estudos.demo.dto.Produto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ProdutoServiceTest {

    @Test
    void deveListarProdutosDeUmaCategoriaExistente() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categorias = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categorias);
        CategoriaEntity categoria = new CategoriaEntity(1L, "Perifericos");
        when(categorias.existsById(1L)).thenReturn(true);
        when(repository.findByCategoriaId(1L)).thenReturn(List.of(
                new ProdutoEntity(1L, "Teclado", 150.0, categoria),
                new ProdutoEntity(2L, "Mouse", 80.0, categoria)));

        assertEquals(List.of(new Produto(1L, "Teclado", 150.0, 1L),
                new Produto(2L, "Mouse", 80.0, 1L)), service.listarPorCategoria(1L));
    }

    @Test
    void deveRetornarListaVaziaParaCategoriaSemProdutos() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categorias = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categorias);
        when(categorias.existsById(1L)).thenReturn(true);
        when(repository.findByCategoriaId(1L)).thenReturn(List.of());

        assertTrue(service.listarPorCategoria(1L).isEmpty());
    }

    @Test
    void deveRetornar404AoListarCategoriaInexistente() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categorias = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categorias);
        when(categorias.existsById(999L)).thenReturn(false);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.listarPorCategoria(999L));
        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
        verify(repository, never()).findByCategoriaId(any());
    }

    @Test
    void deveBuscarProdutoExistentePorId() {
        // Preparação
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categoriaRepository);

        ProdutoEntity entidade = new ProdutoEntity(1L, "Teclado", 150.0, null);
        when(repository.findById(1L)).thenReturn(Optional.of(entidade));

        // Execução
        Optional<Produto> resultado = service.buscarPorId(1L);

        // Verificação
        assertTrue(resultado.isPresent());
        assertEquals("Teclado", resultado.get().nome());
    }

    @Test
    void deveRetornarVazioQuandoProdutoNaoExistir() {
        // Preparação
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categoriaRepository);

        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Execução
        Optional<Produto> resultado = service.buscarPorId(999L);

        // Verificação
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveCriarProduto() {
        // Preparação
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categoriaRepository);

        Produto produto = new Produto(1L, "Teclado", 150.0, null);
        ProdutoEntity entidadeSalva =
                new ProdutoEntity(1L, "Teclado", 150.0, null);

        when(repository.save(any(ProdutoEntity.class)))
                .thenReturn(entidadeSalva);

        // Execução
        Produto resultado = service.criarProduto(produto);

        // Verificação
        assertEquals(produto, resultado);
        
        ArgumentCaptor<ProdutoEntity> captor =
        ArgumentCaptor.forClass(ProdutoEntity.class);

        verify(repository).save(captor.capture());

        ProdutoEntity entidadeEnviada = captor.getValue();

        assertEquals(1L, entidadeEnviada.getId());
        assertEquals("Teclado", entidadeEnviada.getNome());
        assertEquals(150.0, entidadeEnviada.getPreco());
    }

    @Test
    void deveAssociarCategoriaExistenteAoProduto() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categoriaRepository);
        CategoriaEntity categoria = new CategoriaEntity(1L, "Perifericos");
        Produto produto = new Produto(2L, "Mouse", 80.0, 1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(ProdutoEntity.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Produto resultado = service.criarProduto(produto);

        ArgumentCaptor<ProdutoEntity> captor = ArgumentCaptor.forClass(ProdutoEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(1L, captor.getValue().getCategoria().getId());
        assertEquals(1L, resultado.categoriaId());
    }

    @Test
    void deveRecusarProdutoComCategoriaInexistente() {
        ProdutoRepository repository = mock(ProdutoRepository.class);
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        ProdutoService service = new ProdutoService(repository, categoriaRepository);
        Produto produto = new Produto(2L, "Mouse", 80.0, 999L);
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class, () -> service.criarProduto(produto));

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
        verify(repository, never()).save(any(ProdutoEntity.class));
    }
}
