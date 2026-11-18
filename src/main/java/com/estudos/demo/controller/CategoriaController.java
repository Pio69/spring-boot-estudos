package com.estudos.demo.controller;

import com.estudos.demo.service.CategoriaService;
import com.estudos.demo.dto.Categoria;
import com.estudos.demo.service.ProdutoService;
import com.estudos.demo.dto.Produto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;


import jakarta.validation.Valid;

@RestController
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    public CategoriaController(
            CategoriaService categoriaService,
            ProdutoService produtoService) {
        this.categoriaService = categoriaService;
        this.produtoService = produtoService;
    }

    @PostMapping("/categoria")
    public Categoria criarCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaService.criarCategoria(categoria);
    }

    @GetMapping("/categoria")
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/categoria/{id}/produtos")
    public List<Produto> listarProdutosPorCategoria(@PathVariable Long id) {
        return produtoService.listarPorCategoria(id);
    }

}