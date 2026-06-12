package br.com.ifba.projecaosaude.repository;

import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjecaoSaudeRepository extends JpaRepository<ProjecaoSaude, Long> {

    List<ProjecaoSaude> findByUsuario_Id(Long usuarioId);

}
