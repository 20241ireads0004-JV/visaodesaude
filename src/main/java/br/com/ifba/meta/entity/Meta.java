package br.com.ifba.meta.entity;

import br.com.ifba.infraestructure.entity.PersistenceEntity;
import br.com.ifba.meta.enums.CategoriaMeta;
import br.com.ifba.meta.enums.StatusMeta;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "metas")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class Meta extends PersistenceEntity {

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaMeta categoria;

    @Column(nullable = false)
    private LocalDate dataLimite;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusMeta status;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
