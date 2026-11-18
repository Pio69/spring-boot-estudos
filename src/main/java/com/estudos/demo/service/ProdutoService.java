package com.estudos.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.estudos.demo.dto.Produto;
import com.estudos.demo.entity.CategoriaEntity;
import com.estudos.demo.entity.ProdutoEntity;
import com.estudos.demo.repository.CategoriaRepository;
import com.estudos.demo.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(
            ProdutoRepository repository,
            CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto criarProduto(Produto produto) {
        CategoriaEntity categoria = null;

        if (produto.categoriaId() != null) {
            categoria = categoriaRepository.findById(produto.categoriaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Categoria não encontrada"
                    ));
        }

        ProdutoEntity entidade = new ProdutoEntity(
                produto.id(),
                produto.nome(),
                produto.preco(),
                categoria
        );

        ProdutoEntity salvo = repository.save(entidade);
        return paraProduto(salvo);
    }

    public List<Produto> listarProdutos() {
        return repository.findAll().stream()
                .map(this::paraProduto)
                .toList();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::paraProduto);
    }

    public boolean excluirPorId(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private Produto paraProduto(ProdutoEntity entidade) {
        Long categoriaId = entidade.getCategoria() == null
                ? null
                : entidade.getCategoria().getId();

        return new Produto(
                entidade.getId(),
                entidade.getNome(),
                entidade.getPreco(),
                categoriaId
        );
    }

    public List<Produto> listarPorCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada"
            );
        }

        return repository.findByCategoriaId(categoriaId).stream()
                .map(this::paraProduto)
                .toList();
    }
}