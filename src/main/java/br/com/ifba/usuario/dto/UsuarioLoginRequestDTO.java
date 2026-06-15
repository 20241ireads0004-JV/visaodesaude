package br.com.ifba.usuario.dto;

import lombok.Data;

@Data
public class UsuarioLoginRequestDTO {

    private String email;

    private String senha;

}
