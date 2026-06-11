package br.com.ifba.gestorcorporativo.entity;

import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "gestores_corporativos")
@PrimaryKeyJoinColumn(name = "usuario_id") // Liga a chave primária à tabela de usuarios
@NoArgsConstructor
@AllArgsConstructor
public class GestorCorporativo extends Usuario {


    @Column(nullable = false, unique = true)
    private Long idGestor;
}
