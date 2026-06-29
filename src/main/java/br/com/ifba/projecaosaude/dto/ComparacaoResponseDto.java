package br.com.ifba.projecaosaude.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComparacaoResponseDto {

    private Integer riscoAtual;

    private Integer riscoIdeal;

    private Integer diferenca;

    private String mensagem;

}