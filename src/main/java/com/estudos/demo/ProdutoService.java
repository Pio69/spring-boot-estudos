package com.estudos.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final List<Produto> produtos = new ArrayList<>();

    public Produto criarProduto(Produto produto) {
        produtos.add(produto);
        return produto;
    }

    public List<Produto> listarProdutos() {
        return produtos;
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtos.stream()
                .filter(produto -> produto.id().equals(id))
                .findFirst();
    }

    public boolean excluirPorId(Long id) {
        return produtos.removeIf(produto -> produto.id().equals(id));
    }

}