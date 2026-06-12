package br.com.ifba.relatorio.entity;

import br.com.ifba.infraestructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Relatorio extends PersistenceEntity {


    @Column(nullable = false)
    private String tipo;


    @Column(nullable = false)
    private String periodo;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    /** Token UUID para download — expira em 72h */
    @Column(name = "link_compartilhamento", unique = true)
    private String linkCompartilhamento;

    /** Quando o link expira (dataGeracao + 72h) */
    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;


    @Column(name = "caminho_arquivo")
    private String caminhoArquivo;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
}
