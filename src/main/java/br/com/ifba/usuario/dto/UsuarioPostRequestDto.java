package br.com.ifba.usuario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioPostRequestDto {

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

    private Long idEmpresa;
}
