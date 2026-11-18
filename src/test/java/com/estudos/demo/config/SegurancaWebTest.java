package com.estudos.demo.config;

import com.estudos.demo.service.ProdutoService;
import com.estudos.demo.controller.ProdutoController;
import com.estudos.demo.dto.Produto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProdutoController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class SegurancaWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    private static final String PRODUTO_JSON = """
            {
                "id": 3,
                "nome": "Monitor",
                "preco": 900.0
            }
            """;

    @Test
    void deveRecusarPostAutenticadoSemTokenCsrf() throws Exception {
        mockMvc.perform(post("/produto")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirPostAutenticadoComTokenCsrf() throws Exception {
        mockMvc.perform(post("/produto")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRecusarCadastroParaUsuarioDeConsulta() throws Exception {
        mockMvc.perform(post("/produto")
                        .with(user("consulta").roles("CONSULTA"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirCadastroParaAdministrador() throws Exception {
        mockMvc.perform(post("/produto")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveAutenticarAdministradorComSenhaCorreta() throws Exception {
        mockMvc.perform(get("/produto")
                        .with(httpBasic("admin", "admin-teste")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin"));
    }

    @Test
    void deveRejeitarSenhaIncorreta() throws Exception {
        mockMvc.perform(get("/produto")
                        .with(httpBasic("admin", "senha-errada")))
                .andExpect(status().isUnauthorized());
    }
}
