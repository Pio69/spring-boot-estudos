package com.estudos.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto criarProduto(Produto produto) {
        ProdutoEntity entidade = new ProdutoEntity(
                produto.id(), produto.nome(), produto.preco()
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
        return new Produto(
                entidade.getId(),
                entidade.getNome(),
                entidade.getPreco()
        );
    }

}