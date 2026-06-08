package br.com.ifba.alerta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaGetResponseDto implements Serializable {

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("data")
    private LocalDate data;

    @JsonProperty("visualizacao")
    private Boolean visualizacao;
}
