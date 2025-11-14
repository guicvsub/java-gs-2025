package br.com.cashplus.controller;

import br.com.cashplus.dto.OperadorDTO;
import br.com.cashplus.service.OperadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operadores")
public class OperadorController {
    
    @Autowired
    private OperadorService operadorService;
    
    @PostMapping
    public ResponseEntity<OperadorDTO> criar(@Valid @RequestBody OperadorDTO dto) {
        OperadorDTO operadorCriado = operadorService.criar(dto);
        return new ResponseEntity<>(operadorCriado, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<OperadorDTO>> listarTodos() {
        List<OperadorDTO> operadores = operadorService.listarTodos();
        return ResponseEntity.ok(operadores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OperadorDTO> buscarPorId(@PathVariable Long id) {
        OperadorDTO operador = operadorService.buscarPorId(id);
        return ResponseEntity.ok(operador);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OperadorDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OperadorDTO dto) {
        OperadorDTO operadorAtualizado = operadorService.atualizar(id, dto);
        return ResponseEntity.ok(operadorAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        operadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

