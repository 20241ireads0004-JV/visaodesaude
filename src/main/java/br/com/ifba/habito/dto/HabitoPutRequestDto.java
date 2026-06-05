package br.com.ifba.habito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitoPutRequestDto implements Serializable {

    @NotNull(message = "A data é obrigatória")
    @JsonProperty("data")
    private Date data;

    @NotNull(message = "As horas de sono são obrigatórias")
    @Min(value = 0, message = "As horas de sono não podem ser negativas")
    @JsonProperty("horasSono")
    private Integer horasSono;

    @NotNull(message = "A qualidade do sono é obrigatória")
    @Min(value = 1, message = "A qualidade do sono deve ser maior que zero")
    @JsonProperty("qualidadeSono")
    private Integer qualidadeSono;

    @NotBlank(message = "A alimentação é obrigatória")
    @JsonProperty("alimentacao")
    private String alimentacao;

    @NotNull(message = "A intensidade do exercício é obrigatória")
    @Min(value = 0, message = "A intensidade do exercício não pode ser negativa")
    @JsonProperty("exercicioIntensidade")
    private Integer exercicioIntensidade;

    @NotNull(message = "O tipo de exercício é obrigatório")
    @Min(value = 0, message = "O tipo de exercício não pode ser negativo")
    @JsonProperty("exercicioTipo")
    private Integer exercicioTipo;

    @NotNull(message = "A duração do exercício é obrigatória")
    @Min(value = 0, message = "A duração do exercício não pode ser negativa")
    @JsonProperty("exercicioDuracao")
    private Integer exercicioDuracao;
}