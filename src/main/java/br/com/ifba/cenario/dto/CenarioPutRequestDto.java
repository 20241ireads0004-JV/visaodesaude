package br.com.ifba.cenario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenarioPutRequestDto implements Serializable {

    @NotBlank(message = "O tipo é obrigatório.")
    @Size(max = 50, message = "O tipo deve possuir no máximo 50 caracteres.")
    @JsonProperty("tipo")
    private String tipo;

    @Min(value = 0, message = "As horas de sono não podem ser negativas.")
    @Max(value = 24, message = "As horas de sono não podem ser maiores que 24.")
    @JsonProperty("horasSono")
    private int horasSono;

    @NotBlank(message = "A alimentação é obrigatória.")
    @Size(max = 255, message = "A alimentação deve possuir no máximo 255 caracteres.")
    @JsonProperty("alimentacao")
    private String alimentacao;

    @NotBlank(message = "O exercício é obrigatório.")
    @Size(max = 255, message = "O exercício deve possuir no máximo 255 caracteres.")
    @JsonProperty("exercicio")
    private String exercicio;
}
