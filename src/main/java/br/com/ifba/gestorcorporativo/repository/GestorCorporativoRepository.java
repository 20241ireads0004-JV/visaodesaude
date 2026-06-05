package br.com.ifba.gestorcorporativo.repository;

import br.com.ifba.gestorcorporativo.entity.GestorCorporativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestorCorporativoRepository extends JpaRepository<GestorCorporativo, Long> {

    boolean existsByIdGestor(Long idGestor);

    // Método existente (Usado no EDITAR do Service)
    boolean existsByIdGestorAndIdNot(Long idGestor, Long id);
}
