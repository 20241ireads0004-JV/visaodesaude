package br.com.ifba.empresa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Classe que mostra os dados da empresa
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaGetResponseDto {


    private String nome;

    private String cnpj;

    private String numColaboradoresEstimado;

    private String codigoAcesso;

    private boolean isGestor;
}
