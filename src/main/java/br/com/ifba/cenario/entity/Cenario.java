package br.com.ifba.cenario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import br.com.ifba.infraestructure.entity.PersistenceEntity;

import java.io.Serializable;

@Entity
@Table(name = "cenarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Cenario extends PersistenceEntity implements Serializable {


    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "horas_sono", nullable = false)
    private int horasSono;

    @Column(name = "alimentacao", nullable = false)
    private String alimentacao;

    @Column(name = "exercicio", nullable = false)
    private String exercicio;
}