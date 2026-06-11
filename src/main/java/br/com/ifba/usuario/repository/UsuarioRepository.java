package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    boolean existsByEmail(String email);

    /**
     * Retorna todos os funcionários vinculados a uma empresa.
     * Usado no visualizarDados() para saber quem pertence à empresa do gestor.
     */
    List<Usuario> findByEmpresaId(Long idEmpresa);

    /**
     * Igual ao anterior, mas filtrando por departamento.
     * Usado quando o gestor aplica o filtro de departamento no painel.
     */
    List<Usuario> findByEmpresaIdAndDepartamento(Long idEmpresa, String departamento);
}
