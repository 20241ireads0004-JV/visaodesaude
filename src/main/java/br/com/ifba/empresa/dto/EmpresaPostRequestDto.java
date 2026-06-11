package br.com.ifba.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Classe que recebe o nome da empresa
@Data
public class EmpresaPostRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Cnpj é obrigatório")
    private String cnpj;

    private String numColaboradoresEstimado;

    private String codigoAcesso;

    private boolean isGestor;
}
