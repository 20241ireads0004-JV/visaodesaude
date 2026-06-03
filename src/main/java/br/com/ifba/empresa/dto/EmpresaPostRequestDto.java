package br.com.ifba.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Classe que recebe o nome da empresa
@Data
public class EmpresaPostRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
}
