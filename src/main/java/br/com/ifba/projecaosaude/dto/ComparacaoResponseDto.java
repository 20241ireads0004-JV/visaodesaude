package br.com.ifba.projecaosaude.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComparacaoResponseDto {

    @JsonProperty("riscoAtual")
    private Integer riscoAtual;

    @JsonProperty("riscoIdeal")
    private Integer riscoIdeal;

    @JsonProperty("diferenca")
    private Integer diferenca;

    @JsonProperty("mensagem")
    private String mensagem;

}