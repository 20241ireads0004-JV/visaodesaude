package br.com.ifba.projecaosaude.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import br.com.ifba.infraestructure.entity.PersistenceEntity;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "projecoes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjecaoSaude extends PersistenceEntity implements Serializable {


    @Column(name = "risco_cardio_vascular", nullable = false)
    private int riscoCardioVascular;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "visualizacao", nullable = false)
    private Boolean visualizacao;
}
