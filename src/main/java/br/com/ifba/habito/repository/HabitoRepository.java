package br.com.ifba.habito.repository;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.habito.entity.Habito;
import br.com.ifba.relatorio.entity.Relatorio;
import br.com.ifba.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Long> {

    /**
     * Média de horas de sono de um grupo de usuários
     */
    @Query("SELECT AVG(h.horasSono) FROM Habito h WHERE h.usuario.id IN :ids")
    Double calcularMediaSono(@Param("ids") List<Long> ids);


    /**
     * Média de qualidade do sono (escala 1–5) de um grupo de usuários
     */
    @Query("SELECT AVG(h.qualidadeSono) FROM Habito h WHERE h.usuario.id IN :ids")
    Double calcularMediaQualidade(@Param("ids") List<Long> ids);

    /**
     * Quantos usuários (do grupo) têm ao menos 1 hábito registrado
     */
    @Query("SELECT COUNT(DISTINCT h.usuario.id) FROM Habito h WHERE h.usuario.id IN :ids")
    Long contarComRegistro(@Param("ids") List<Long> ids);

    /**
     * Evolução mensal da média de sono — retorna [mês (int), média (double)]
     * Ordenado cronologicamente. Usado no gráfico de linha do painel.
     */
    @Query(value = """
        SELECT MONTH(h.data) AS mes, AVG(h.horas_sono) AS media
        FROM habitos h
        WHERE h.usuario_id IN (:ids)
        GROUP BY YEAR(h.data), MONTH(h.data)
        ORDER BY YEAR(h.data) ASC, MONTH(h.data) ASC
        LIMIT 6
    """, nativeQuery = true)
    List<Object[]> calcularEvolucaoMensal(@Param("ids") List<Long> ids);

/**
 * Risco por departamento — retorna [departamento, riscoAlto, riscoMedio, riscoBaixo]
 */
    @Query(value = """
                SELECT
                    u.departamento,
                    SUM(CASE WHEN avg_sono < 6 THEN 1 ELSE 0 END)                          AS risco_alto,
                    SUM(CASE WHEN avg_sono >= 6 AND avg_sono < 7 THEN 1 ELSE 0 END)        AS risco_medio,
                    SUM(CASE WHEN avg_sono >= 7 THEN 1 ELSE 0 END)                         AS risco_baixo
                FROM (
                    SELECT h.usuario_id, AVG(h.horas_sono) AS avg_sono
                    FROM habitos h
                    GROUP BY h.usuario_id
                ) AS medias
                JOIN usuarios u ON u.id = medias.usuario_id
                WHERE u.empresa_id = :empresaId
                GROUP BY u.departamento
            """, nativeQuery = true)
    List<Object[]> calcularRiscoPorDepartamento(@Param("empresaId") Long empresaId);

    List<Habito> findByUsuario_IdAndDataBetweenOrderByDataAsc(Long usuarioId, Date dataInicio, Date dataFim);

    Optional<Habito> findFirstByUsuarioIdOrderByDataDesc(Long usuarioId);

    Optional<Habito> findTopByUsuarioIdOrderByIdDesc(Long usuarioId);
}
