package br.com.ifba.projecaosaude.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoResponseDto {

    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("prioridade")
    private String prioridade;

    @JsonProperty("impacto")
    private String impacto;
}