package com.estudos.demo.service;

import com.estudos.demo.repository.CategoriaRepository;
import com.estudos.demo.entity.CategoriaEntity;
import com.estudos.demo.dto.Categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Categoria criarCategoria(Categoria categoria) {
        CategoriaEntity entidade = new CategoriaEntity(
                categoria.id(), categoria.nome()
        );

        CategoriaEntity salva = repository.save(entidade);
        return paraCategoria(salva);
    }

    public List<Categoria> listarCategorias() {
        return repository.findAll().stream()
                .map(this::paraCategoria)
                .toList();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::paraCategoria);
    }

    public boolean excluirPorId(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private Categoria paraCategoria(CategoriaEntity entidade) {
        return new Categoria(
                entidade.getId(),
                entidade.getNome()
        );
    }

}