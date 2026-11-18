package com.estudos.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.estudos.demo.entity.CategoriaEntity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "produtos")
public class ProdutoEntity {

    @Id
    private Long id;

    private String nome;
    private double preco;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;

    protected ProdutoEntity() {
    }

    public ProdutoEntity(
            Long id,
            String nome,
            double preco,
            CategoriaEntity categoria) {

        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }
}