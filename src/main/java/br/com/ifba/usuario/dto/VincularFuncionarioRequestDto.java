package br.com.ifba.usuario.dto;

import lombok.Data;

@Data
public class VincularFuncionarioRequestDto {

    /** Código fornecido pelo RH — Ex: "EMP-A1B2C3D4" */
    private String codigoEmpresa;

    /** Departamento escolhido pelo funcionário — Ex: "TI", "Financeiro" */
    private String departamento;

}
