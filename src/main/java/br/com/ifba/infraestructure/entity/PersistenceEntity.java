package br.com.ifba.infraestructure.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.Id;

@MappedSuperclass
@Data
public class PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}