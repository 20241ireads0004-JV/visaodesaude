package br.com.ifba.habito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class HabitoGetResponseDto implements Serializable {

    @JsonProperty("data")
    private Date data;

    @JsonProperty("horasSono")
    private Integer horasSono;

    @JsonProperty("qualidadeSono")
    private Integer qualidadeSono;

    @JsonProperty("alimentacao")
    private String alimentacao;

    @JsonProperty("exercicioIntensidade")
    private Integer exercicioIntensidade;

    @JsonProperty("exercicioTipo")
    private Integer exercicioTipo;

    @JsonProperty("exercicioDuracao")
    private Integer exercicioDuracao;
}
