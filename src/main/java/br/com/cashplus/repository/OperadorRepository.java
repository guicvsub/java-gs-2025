package br.com.cashplus.repository;

import br.com.cashplus.model.Operador;
import br.com.cashplus.model.valueobject.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
    
    @Query(value = "SELECT * FROM operadores WHERE cpf = :cpf", nativeQuery = true)
    Optional<Operador> findByCpf(@Param("cpf") String cpf);
    
    @Query(value = "SELECT COUNT(*) FROM operadores WHERE cpf = :cpfValor", nativeQuery = true)
    Long countByCpfValor(@Param("cpfValor") String cpfValor);
    
    default boolean existsByCpfValor(String cpfValor) {
        Long count = countByCpfValor(cpfValor);
        return count != null && count > 0;
    }
    
    default boolean existsByCpf(CPF cpf) {
        return existsByCpfValor(cpf.getValor());
    }
    
    default boolean existsByCpf(String cpf) {
        return existsByCpf(CPF.of(cpf));
    }
}

