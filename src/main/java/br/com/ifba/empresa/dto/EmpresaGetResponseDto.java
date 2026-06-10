package br.com.ifba.empresa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Classe que mostra os dados da empresa
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaGetResponseDto {

    private Long id;
    private String nome;

}
