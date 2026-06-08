package br.com.ifba.projecaosaude.dto;

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
public class ProjecaoSaudeGetResponseDto implements  Serializable {

    @JsonProperty("riscoCardioVascular")
    private int riscoCardioVascular;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("data")
    private LocalDate data;

    @JsonProperty("visualizacao")
    private Boolean visualizacao;
}