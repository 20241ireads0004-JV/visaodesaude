package br.com.ifba.gestorcorporativo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GestorCorporativoGetResponseDto {

    private Long id;

    private String nome;

    private String email;

    private Integer idade;

    private String sexo;

    private String nomeEmpresa;

    private Long idGestor;
}
