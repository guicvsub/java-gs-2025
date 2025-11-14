package br.com.cashplus.repository;

import br.com.cashplus.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
    
    Optional<Operador> findByCpf(String cpf);
    
    boolean existsByCpf(String cpf);
}

