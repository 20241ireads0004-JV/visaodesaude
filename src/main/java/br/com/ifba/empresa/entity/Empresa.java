package br.com.ifba.empresa.entity;

import br.com.ifba.infraestructure.entity.PersistenceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "empresas")
@NoArgsConstructor
@AllArgsConstructor
public class Empresa extends PersistenceEntity {


    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cnpj;

    @Column(name = "codigo_acesso", unique = true)
    private String codigoAcesso; // ex: "EMP-A1B2C3D4"

    @Column(name = "gestor_id")
    private Long gestorId; // ID do usuário que cadastrou a empresa
}