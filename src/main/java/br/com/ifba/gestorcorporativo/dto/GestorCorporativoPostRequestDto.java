package br.com.ifba.gestorcorporativo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GestorCorporativoPostRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 1, message = "Idade inválida")
    private Integer idade;

    @NotBlank(message = "Sexo é obrigatório")
    private String sexo;

    @NotNull(message = "Empresa é obrigatória")
    private Long idEmpresa;

    @NotNull(message = "O ID de Gestor é obrigatório")
    private Long idGestor;
}
