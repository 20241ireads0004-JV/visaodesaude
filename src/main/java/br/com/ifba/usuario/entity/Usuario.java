package br.com.ifba.usuario.entity;

import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.infraestructure.entity.PersistenceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends PersistenceEntity {


    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = true)
    private String departamento; // preenchido quando o funcionário se vincula

    private Integer idade;

    private String sexoBiologico;

    // Regra de Negócio: Chave Estrangeira obrigatória para Empresa
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = true)
    private Empresa empresa;

    private Boolean gestor = false;

}
