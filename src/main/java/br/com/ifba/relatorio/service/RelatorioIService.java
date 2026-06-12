package br.com.ifba.relatorio.service;

import br.com.ifba.relatorio.dto.RelatorioGetResponseDto;
import br.com.ifba.relatorio.dto.RelatorioPostRequestDto;

public interface RelatorioIService {

    byte[] gerarPdf(RelatorioPostRequestDto dto, Long usuarioId);

    RelatorioGetResponseDto compartilhar(RelatorioPostRequestDto dto, Long usuarioId);

    byte[] downloadPorToken(String token);

}

