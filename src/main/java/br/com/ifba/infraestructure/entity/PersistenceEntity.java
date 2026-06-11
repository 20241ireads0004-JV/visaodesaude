package br.com.ifba.infraestructure.entity;

import jakarta.persistence.Id; // <-- IMPORT CORRETO (Substitua o antigo por este)
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}