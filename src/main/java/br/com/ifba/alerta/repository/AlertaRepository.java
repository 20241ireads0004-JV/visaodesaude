package br.com.ifba.alerta.repository;

import br.com.ifba.alerta.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByUsuario_IdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

}
