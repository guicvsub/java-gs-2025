package br.com.cashplus.controller;

import br.com.cashplus.dto.request.TransacaoRequestDTO;
import br.com.cashplus.dto.response.TransacaoResponseDTO;
import br.com.cashplus.service.TransacaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Transações.
 * Interface REST da aplicação.
 */
@RestController
@RequestMapping("/api/transacoes")
@Validated
public class TransacaoController {
    
    @Autowired
    private TransacaoService transacaoService;
    
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO requestDTO) {
        TransacaoResponseDTO transacaoCriada = transacaoService.criar(requestDTO);
        return new ResponseEntity<>(transacaoCriada, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listarTodas() {
        List<TransacaoResponseDTO> transacoes = transacaoService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(
            @PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        TransacaoResponseDTO transacao = transacaoService.buscarPorId(id);
        return ResponseEntity.ok(transacao);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable @Positive(message = "ID deve ser positivo") Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

