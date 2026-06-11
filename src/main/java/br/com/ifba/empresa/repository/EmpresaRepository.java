package br.com.ifba.empresa.repository;

import br.com.ifba.empresa.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /** Busca a empresa pelo código de acesso informado pelo funcionário */
    Optional<Empresa> findByCodigoAcesso(String codigoAcesso);

    /** Verifica se o usuário já cadastrou uma empresa (é gestor) */
    Optional<Empresa> findByGestorId(Long gestorId);

    /** Evita CNPJ duplicado no cadastro */
    boolean existsByCnpj(String cnpj);
}
