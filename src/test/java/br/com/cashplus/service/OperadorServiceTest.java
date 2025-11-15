package br.com.cashplus.service;

import br.com.cashplus.dto.request.OperadorRequestDTO;
import br.com.cashplus.dto.response.OperadorResponseDTO;
import br.com.cashplus.exception.BusinessException;
import br.com.cashplus.model.Operador;
import br.com.cashplus.model.enums.TurnoEnum;
import br.com.cashplus.model.valueobject.CPF;
import br.com.cashplus.repository.OperadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperadorServiceTest {

    @Mock
    private OperadorRepository operadorRepository;

    @InjectMocks
    private OperadorService operadorService;

    private OperadorRequestDTO operadorRequestDTO;
    private Operador operador;

    @BeforeEach
    void setUp() {
        // Usando CPF válido para testes: 111.444.777-35
        operadorRequestDTO = new OperadorRequestDTO("João da Silva", "111.444.777-35", TurnoEnum.MANHA);
        operador = new Operador(1L, "João da Silva", CPF.of("111.444.777-35"), TurnoEnum.MANHA);
    }

    @Test
    @DisplayName("Deve criar um novo operador com sucesso")
    void criar_DeveCriarOperadorComSucesso() {
        // Arrange
        when(operadorRepository.existsByCpf(any(CPF.class))).thenReturn(false);
        when(operadorRepository.save(any(Operador.class))).thenReturn(operador);

        // Act
        OperadorResponseDTO resultado = operadorService.criar(operadorRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João da Silva", resultado.getNome());
        verify(operadorRepository, times(1)).existsByCpf(any(CPF.class));
        verify(operadorRepository, times(1)).save(any(Operador.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar criar operador com CPF duplicado")
    void criar_DeveLancarExcecao_QuandoCPFDuplicado() {
        // Arrange
        when(operadorRepository.existsByCpf(any(CPF.class))).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            operadorService.criar(operadorRequestDTO);
        });

        assertTrue(exception.getMessage().contains("CPF já cadastrado"));
        verify(operadorRepository, times(1)).existsByCpf(any(CPF.class));
        verify(operadorRepository, never()).save(any(Operador.class));
    }
}
