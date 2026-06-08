package br.com.ifba.cenario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenarioGetResponseDto implements Serializable {

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("horasSono")
    private int horasSono;

    @JsonProperty("alimentacao")
    private String alimentacao;

    @JsonProperty("exercicio")
    private String exercicio;
}
