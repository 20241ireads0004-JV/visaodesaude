package br.com.ifba.relatorio.controller;

import br.com.ifba.relatorio.dto.RelatorioGetResponseDto;
import br.com.ifba.relatorio.dto.RelatorioPostRequestDto;
import br.com.ifba.relatorio.service.RelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * 1. Baixa o PDF diretamente.
     * Retorna o arquivo binário (PDF) para o navegador iniciar o download.
     */
    @PostMapping("/gerarpdf/{usuarioId}")
    public ResponseEntity<byte[]> gerarPdf(
            @PathVariable Long usuarioId,
            @RequestBody RelatorioPostRequestDto dto) {

        byte[] pdfBytes = relatorioService.gerarPdf(dto, usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Define o nome do arquivo no download
        headers.setContentDispositionFormData("attachment", "relatorio.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * 2. Gera um link de compartilhamento válido por 72h.
     * Retorna o JSON com o token, metadados e a URL de acesso.
     */
    @PostMapping("/compartilhar/{usuarioId}")
    public ResponseEntity<RelatorioGetResponseDto> compartilhar(
            @PathVariable Long usuarioId,
            @RequestBody RelatorioPostRequestDto dto) {

        RelatorioGetResponseDto responseDto = relatorioService.compartilhar(dto, usuarioId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 3. Endpoint de Download público via Token (Link compartilhado).
     * Note que a rota mapeia o "/relatorios/download/{token}" que você estruturou no Service.
     */
    @GetMapping("/download/{token}")
    public ResponseEntity<byte[]> downloadPorToken(@PathVariable String token) {

        byte[] pdfBytes = relatorioService.downloadPorToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-compartilhado.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
