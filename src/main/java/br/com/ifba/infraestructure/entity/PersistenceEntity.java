package br.com.ifba.infraestructure.entity;


import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class PersistenceEntity {

    @Id // Agora sim o Hibernate sabe que isso é uma Chave Primária!
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}