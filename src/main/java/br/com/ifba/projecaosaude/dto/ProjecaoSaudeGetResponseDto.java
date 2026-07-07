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

    @JsonProperty("data")
    private LocalDate data;

    @JsonProperty("atual")
    private Integer atual;

    @JsonProperty("projetado")
    private Integer projetado;

    @JsonProperty("imc")
    private Integer imc;

    @JsonProperty("sono")
    private String sono;

    @JsonProperty("energia")
    private String energia;

}