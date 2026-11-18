package com.estudos.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Categoria(
        @NotNull(message = "O ID é obrigatório")
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        String nome

) {
}