package br.com.cashplus.service;

import br.com.cashplus.dto.TransacaoDTO;
import br.com.cashplus.exception.ResourceNotFoundException;
import br.com.cashplus.model.Transacao;
import br.com.cashplus.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    @Transactional
    public TransacaoDTO criar(TransacaoDTO dto) {
        Transacao transacao = new Transacao();
        transacao.setValor(dto.getValor());
        transacao.setTipoPagamento(dto.getTipoPagamento().toUpperCase());
        
        // Cálculo automático do risco de fraude (regra de negócio isolada)
        String riscoFraude = calcularRiscoFraude(dto.getValor(), dto.getTipoPagamento());
        transacao.setRiscoFraude(riscoFraude);
        
        transacao = transacaoRepository.save(transacao);
        return toDTO(transacao);
    }
    
    public List<TransacaoDTO> listarTodas() {
        return transacaoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public TransacaoDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));
        return toDTO(transacao);
    }
    
    @Transactional
    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transação não encontrada com ID: " + id);
        }
        transacaoRepository.deleteById(id);
    }
    
    /**
     * Calcula o risco de fraude baseado em regras de negócio
     * Regras:
     * - DINHEIRO: sempre BAIXO
     * - PIX: sempre BAIXO
     * - CARTAO: BAIXO se valor < 100, MEDIO se 100-500, ALTO se > 500
     */
    private String calcularRiscoFraude(BigDecimal valor, String tipoPagamento) {
        if (tipoPagamento == null) {
            return "MEDIO";
        }
        
        String tipo = tipoPagamento.toUpperCase();
        
        if ("DINHEIRO".equals(tipo) || "PIX".equals(tipo)) {
            return "BAIXO";
        }
        
        if ("CARTAO".equals(tipo)) {
            BigDecimal cem = new BigDecimal("100");
            BigDecimal quinhentos = new BigDecimal("500");
            if (valor.compareTo(cem) < 0) {
                return "BAIXO";
            } else if (valor.compareTo(quinhentos) <= 0) {
                return "MEDIO";
            } else {
                return "ALTO";
            }
        }
        
        return "MEDIO";
    }
    
    private TransacaoDTO toDTO(Transacao transacao) {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setId(transacao.getId());
        dto.setValor(transacao.getValor());
        dto.setTipoPagamento(transacao.getTipoPagamento());
        dto.setRiscoFraude(transacao.getRiscoFraude());
        dto.setDataTransacao(transacao.getDataTransacao());
        return dto;
    }
}

