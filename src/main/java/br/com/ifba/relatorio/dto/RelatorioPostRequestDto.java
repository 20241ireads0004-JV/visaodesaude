package br.com.ifba.relatorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class RelatorioPostRequestDto {

    private String periodo;

    /**
     * Seções marcadas para incluir no PDF.
     * Valores possíveis: "RESUMO", "GRAFICOS", "ALERTAS", "PROJECOES"
     * Exemplo: ["RESUMO", "GRAFICOS", "ALERTAS"]
     */
    private List<String> secoes;


}
