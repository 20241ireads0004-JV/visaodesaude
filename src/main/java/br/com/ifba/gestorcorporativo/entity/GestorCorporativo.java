package br.com.ifba.gestorcorporativo.entity;

import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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

    // Código corporativo exclusivo do Gestor/RH
    @Column(nullable = false, unique = true)
    private Long idGestor;
}
