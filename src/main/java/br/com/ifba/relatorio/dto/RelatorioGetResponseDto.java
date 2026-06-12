package br.com.ifba.relatorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioGetResponseDto {


    private String tipo;

    private String periodo;

    private LocalDateTime dataGeracao;

    private String linkCompartilhamento;

    private LocalDateTime dataExpiracao;

    private String mensagem;
}
