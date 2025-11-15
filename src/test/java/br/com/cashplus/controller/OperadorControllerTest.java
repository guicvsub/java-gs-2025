package br.com.cashplus.controller;

import br.com.cashplus.dto.request.OperadorRequestDTO;
import br.com.cashplus.dto.response.OperadorResponseDTO;
import br.com.cashplus.model.enums.TurnoEnum;
import br.com.cashplus.service.OperadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OperadorController.class, 
            excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
            })
class OperadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OperadorService operadorService;

    @Test
    @DisplayName("POST /api/operadores - Deve criar um operador e retornar 201 Created")
    void criarOperador_DeveRetornar201_QuandoSucesso() throws Exception {
        // Arrange
        OperadorRequestDTO requestDTO = new OperadorRequestDTO("Maria Teste", "987.654.321-00", TurnoEnum.TARDE);
        OperadorResponseDTO responseDTO = new OperadorResponseDTO(1L, "Maria Teste", "987.654.321-00", TurnoEnum.TARDE, "Tarde");

        when(operadorService.criar(any(OperadorRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/operadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Maria Teste"));
    }

    @Test
    @DisplayName("POST /api/operadores - Deve retornar 400 Bad Request quando o nome for inválido")
    void criarOperador_DeveRetornar400_QuandoNomeInvalido() throws Exception {
        // Arrange
        OperadorRequestDTO requestDTO = new OperadorRequestDTO("Ma", "987.654.321-00", TurnoEnum.TARDE); // Nome muito curto

        // Act & Assert
        mockMvc.perform(post("/api/operadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").exists());
    }
}
