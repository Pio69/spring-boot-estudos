package com.estudos.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Produto(
        @NotNull(message = "O ID é obrigatório")
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Positive(message = "O preço deve ser maior que zero")
        double preco
) {
}