package br.com.cashplus.service;

import br.com.cashplus.dto.OperadorDTO;
import br.com.cashplus.exception.BusinessException;
import br.com.cashplus.exception.ResourceNotFoundException;
import br.com.cashplus.model.Operador;
import br.com.cashplus.repository.OperadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperadorService {
    
    @Autowired
    private OperadorRepository operadorRepository;
    
    @Transactional
    public OperadorDTO criar(OperadorDTO dto) {
        // Validação de CPF duplicado
        if (operadorRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF já cadastrado: " + dto.getCpf());
        }
        
        Operador operador = new Operador();
        operador.setNome(dto.getNome());
        operador.setCpf(dto.getCpf().replaceAll("[^0-9]", ""));
        operador.setTurno(dto.getTurno().toUpperCase());
        
        operador = operadorRepository.save(operador);
        return toDTO(operador);
    }
    
    public List<OperadorDTO> listarTodos() {
        return operadorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public OperadorDTO buscarPorId(Long id) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operador não encontrado com ID: " + id));
        return toDTO(operador);
    }
    
    @Transactional
    public OperadorDTO atualizar(Long id, OperadorDTO dto) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operador não encontrado com ID: " + id));
        
        // Validação de CPF duplicado (se mudou)
        if (!operador.getCpf().equals(dto.getCpf().replaceAll("[^0-9]", ""))) {
            if (operadorRepository.existsByCpf(dto.getCpf())) {
                throw new BusinessException("CPF já cadastrado: " + dto.getCpf());
            }
        }
        
        operador.setNome(dto.getNome());
        operador.setCpf(dto.getCpf().replaceAll("[^0-9]", ""));
        operador.setTurno(dto.getTurno().toUpperCase());
        
        operador = operadorRepository.save(operador);
        return toDTO(operador);
    }
    
    @Transactional
    public void deletar(Long id) {
        if (!operadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Operador não encontrado com ID: " + id);
        }
        operadorRepository.deleteById(id);
    }
    
    private OperadorDTO toDTO(Operador operador) {
        OperadorDTO dto = new OperadorDTO();
        dto.setId(operador.getId());
        dto.setNome(operador.getNome());
        dto.setCpf(operador.getCpf());
        dto.setTurno(operador.getTurno());
        return dto;
    }
}

