package br.com.cashplus.service;

import br.com.cashplus.dto.request.TransacaoRequestDTO;
import br.com.cashplus.dto.response.TransacaoResponseDTO;
import br.com.cashplus.exception.ResourceNotFoundException;
import br.com.cashplus.model.Operador;
import br.com.cashplus.model.Transacao;
import br.com.cashplus.model.enums.RiscoFraudeEnum;
import br.com.cashplus.repository.OperadorRepository;
import br.com.cashplus.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para gerenciamento de Transações.
 * Contém lógica de aplicação e orquestração.
 */
@Service
public class TransacaoService {
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    @Autowired
    private OperadorRepository operadorRepository;
    
    @Autowired
    private RiscoFraudeService riscoFraudeService;
    
    @Transactional
    public TransacaoResponseDTO criar(TransacaoRequestDTO requestDTO) {
        Transacao transacao = new Transacao();
        transacao.setValor(requestDTO.getValor());
        transacao.setTipoPagamento(requestDTO.getTipoPagamento());
        
        // Consulta risco de fraude (pode usar API externa ou cálculo local)
        RiscoFraudeEnum riscoFraude = riscoFraudeService.consultarRisco(
                requestDTO.getValor(), 
                requestDTO.getTipoPagamento()
        );
        transacao.setRiscoFraude(riscoFraude);
        
        // Associa operador se fornecido
        if (requestDTO.getOperadorId() != null) {
            Operador operador = operadorRepository.findById(requestDTO.getOperadorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Operador não encontrado com ID: " + requestDTO.getOperadorId()));
            transacao.setOperador(operador);
        }
        
        transacao = transacaoRepository.save(transacao);
        return toResponseDTO(transacao);
    }
    
    public List<TransacaoResponseDTO> listarTodas() {
        return transacaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public TransacaoResponseDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));
        return toResponseDTO(transacao);
    }
    
    @Transactional
    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transação não encontrada com ID: " + id);
        }
        transacaoRepository.deleteById(id);
    }
    
    private TransacaoResponseDTO toResponseDTO(Transacao transacao) {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setValor(transacao.getValor());
        dto.setTipoPagamento(transacao.getTipoPagamento());
        dto.setTipoPagamentoDescricao(
                transacao.getTipoPagamento() != null ? 
                transacao.getTipoPagamento().getDescricao() : null);
        dto.setRiscoFraude(transacao.getRiscoFraude());
        dto.setRiscoFraudeDescricao(
                transacao.getRiscoFraude() != null ? 
                transacao.getRiscoFraude().getDescricao() : null);
        dto.setDataTransacao(transacao.getDataTransacao());
        
        if (transacao.getOperador() != null) {
            dto.setOperadorId(transacao.getOperador().getId());
            dto.setOperadorNome(transacao.getOperador().getNome());
        }
        
        return dto;
    }
}

