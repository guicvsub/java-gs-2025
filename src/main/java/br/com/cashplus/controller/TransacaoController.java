package br.com.cashplus.controller;

import br.com.cashplus.dto.TransacaoDTO;
import br.com.cashplus.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    
    @Autowired
    private TransacaoService transacaoService;
    
    @PostMapping
    public ResponseEntity<TransacaoDTO> criar(@Valid @RequestBody TransacaoDTO dto) {
        TransacaoDTO transacaoCriada = transacaoService.criar(dto);
        return new ResponseEntity<>(transacaoCriada, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> listarTodas() {
        List<TransacaoDTO> transacoes = transacaoService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO> buscarPorId(@PathVariable Long id) {
        TransacaoDTO transacao = transacaoService.buscarPorId(id);
        return ResponseEntity.ok(transacao);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

