package br.com.cashplus.controller;

import br.com.cashplus.dto.request.OperadorRequestDTO;
import br.com.cashplus.dto.response.OperadorResponseDTO;
import br.com.cashplus.service.OperadorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Operadores.
 * Interface REST da aplicação.
 */
@RestController
@RequestMapping("/api/operadores")
@Validated
public class OperadorController {
    
    @Autowired
    private OperadorService operadorService;
    
    @PostMapping
    public ResponseEntity<OperadorResponseDTO> criar(@Valid @RequestBody OperadorRequestDTO requestDTO) {
        OperadorResponseDTO operadorCriado = operadorService.criar(requestDTO);
        return new ResponseEntity<>(operadorCriado, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<OperadorResponseDTO>> listarTodos() {
        List<OperadorResponseDTO> operadores = operadorService.listarTodos();
        return ResponseEntity.ok(operadores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> buscarPorId(
            @PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        OperadorResponseDTO operador = operadorService.buscarPorId(id);
        return ResponseEntity.ok(operador);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> atualizar(
            @PathVariable @Positive(message = "ID deve ser positivo") Long id,
            @Valid @RequestBody OperadorRequestDTO requestDTO) {
        OperadorResponseDTO operadorAtualizado = operadorService.atualizar(id, requestDTO);
        return ResponseEntity.ok(operadorAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        operadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

