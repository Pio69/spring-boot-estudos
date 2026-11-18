package com.estudos.demo.repository;

import com.estudos.demo.entity.CategoriaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
}