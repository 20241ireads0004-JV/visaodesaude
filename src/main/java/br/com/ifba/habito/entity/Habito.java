package br.com.ifba.habito.entity;

import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import br.com.ifba.infraestructure.entity.PersistenceEntity;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "habitos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Habito extends PersistenceEntity implements Serializable {


    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "horas_sono", nullable = false)
    private int horasSono;

    @Column(name = "qualidade_sono", nullable = false)
    private int qualidadeSono;

    @Column(name = "alimentacao", nullable = false)
    private String alimentacao;

    @Column(name = "exercicio_intensidade", nullable = false)
    private int exercicioIntensidade;

    @Column(name = "exercicio_tipo", nullable = false)
    private int exercicioTipo;

    @Column(name = "exercicio_duracao", nullable = false)
    private int exercicioDuracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "agua_copos", nullable = false)
    private int aguaCopos;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private Double altura;
}
