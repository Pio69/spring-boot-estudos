package com.estudos.demo.controller;

import com.estudos.demo.service.ProdutoService;
import com.estudos.demo.dto.Produto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ProdutoControllerTest {

    @Test
    void deveRetornar404QuandoProdutoNaoExistir() {
        // Preparação
        ProdutoService service = mock(ProdutoService.class);
        ProdutoController controller = new ProdutoController(service);

        when(service.buscarPorId(999L)).thenReturn(Optional.empty());

        // Execução
        ResponseEntity<Produto> resposta = controller.buscarPorId(999L);

        // Verificação
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertNull(resposta.getBody());
    }
}