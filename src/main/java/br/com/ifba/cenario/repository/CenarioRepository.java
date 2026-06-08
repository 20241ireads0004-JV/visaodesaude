package br.com.ifba.cenario.repository;

import br.com.ifba.cenario.entity.Cenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenarioRepository extends JpaRepository<Cenario, Long> {
}
