package br.com.cashplus.service;

import br.com.cashplus.dto.request.OperadorRequestDTO;
import br.com.cashplus.dto.response.OperadorResponseDTO;
import br.com.cashplus.exception.BusinessException;
import br.com.cashplus.exception.ResourceNotFoundException;
import br.com.cashplus.model.Operador;
import br.com.cashplus.model.valueobject.CPF;
import br.com.cashplus.repository.OperadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para gerenciamento de Operadores.
 * Contém lógica de aplicação e orquestração.
 */
@Service
public class OperadorService {
    
    @Autowired
    private OperadorRepository operadorRepository;
    
    @Transactional
    public OperadorResponseDTO criar(OperadorRequestDTO requestDTO) {
        // Validação de CPF duplicado
        CPF cpf = CPF.of(requestDTO.getCpf());
        if (operadorRepository.existsByCpf(cpf)) {
            throw new BusinessException("CPF já cadastrado: " + cpf.getFormatado());
        }
        
        Operador operador = new Operador();
        operador.setNome(requestDTO.getNome());
        operador.setCpf(cpf);
        operador.setTurno(requestDTO.getTurno());
        
        operador = operadorRepository.save(operador);
        return toResponseDTO(operador);
    }
    
    public List<OperadorResponseDTO> listarTodos() {
        return operadorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public OperadorResponseDTO buscarPorId(Long id) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operador não encontrado com ID: " + id));
        return toResponseDTO(operador);
    }
    
    @Transactional
    public OperadorResponseDTO atualizar(Long id, OperadorRequestDTO requestDTO) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operador não encontrado com ID: " + id));
        
        // Validação de CPF duplicado (se mudou)
        CPF novoCpf = CPF.of(requestDTO.getCpf());
        if (!operador.getCpf().equals(novoCpf)) {
            if (operadorRepository.existsByCpf(novoCpf)) {
                throw new BusinessException("CPF já cadastrado: " + novoCpf.getFormatado());
            }
        }
        
        operador.setNome(requestDTO.getNome());
        operador.setCpf(novoCpf);
        operador.setTurno(requestDTO.getTurno());
        
        operador = operadorRepository.save(operador);
        return toResponseDTO(operador);
    }
    
    @Transactional
    public void deletar(Long id) {
        if (!operadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Operador não encontrado com ID: " + id);
        }
        operadorRepository.deleteById(id);
    }
    
    private OperadorResponseDTO toResponseDTO(Operador operador) {
        OperadorResponseDTO dto = new OperadorResponseDTO();
        dto.setId(operador.getId());
        dto.setNome(operador.getNome());
        dto.setCpf(operador.getCpf().getFormatado());
        dto.setTurno(operador.getTurno());
        dto.setTurnoDescricao(operador.getTurno().getDescricao());
        return dto;
    }
}

