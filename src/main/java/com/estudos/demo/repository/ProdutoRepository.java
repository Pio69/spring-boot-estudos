package com.estudos.demo.repository;

import java.util.List;
import com.estudos.demo.entity.ProdutoEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    List<ProdutoEntity> findByCategoriaId(Long categoriaId);
    
}