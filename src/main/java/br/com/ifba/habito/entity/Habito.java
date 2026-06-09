package br.com.ifba.habito.entity;

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


    @Column(name = "id_habito", nullable = false, unique = true)
    private Long idHabito;

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
}
