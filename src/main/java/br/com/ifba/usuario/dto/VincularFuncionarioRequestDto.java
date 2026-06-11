package br.com.ifba.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VincularFuncionarioRequestDto {

    /** Código fornecido pelo RH — Ex: "EMP-A1B2C3D4" */
    @NotBlank(message = "Código da empresa é obrigatório")
    private String codigoEmpresa;

    /** Departamento escolhido pelo funcionário — Ex: "TI", "Financeiro" */
    private String departamento;

}
