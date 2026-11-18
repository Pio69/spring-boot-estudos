package com.estudos.demo.controller;

import com.estudos.demo.service.ProdutoService;
import com.estudos.demo.config.SecurityConfig;
import com.estudos.demo.dto.Produto;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProdutoController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class ProdutoControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void deveRetornar404QuandoProdutoNaoExistir() throws Exception {
        // Preparação
        when(produtoService.buscarPorId(999L))
                .thenReturn(Optional.empty());

        // Execução e verificação
        mockMvc.perform(get("/produto/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProdutoQuandoEncontrado() throws Exception {
        // Preparação
        Produto produto = new Produto(1L, "Teclado", 150.0, null);

        when(produtoService.buscarPorId(1L))
                .thenReturn(Optional.of(produto));

        // Execução e verificação
        mockMvc.perform(get("/produto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Teclado"))
                .andExpect(jsonPath("$.preco").value(150.0));
    }

    @Test
    void deveRejeitarProdutoInvalido() throws Exception {
        String jsonInvalido = """
                {
                    "id": 3,
                    "nome": "",
                    "preco": -10
                }
                """;

        mockMvc.perform(post("/produto")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").exists())
                .andExpect(jsonPath("$.preco").exists());

        verify(produtoService, never()).criarProduto(any(Produto.class));
    }
}
