package br.com.ifba.relatorio.repository;

import br.com.ifba.relatorio.entity.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    /** Busca pelo token UUID para download via link compartilhável */
    Optional<Relatorio> findByLinkCompartilhamento(String token);
}
