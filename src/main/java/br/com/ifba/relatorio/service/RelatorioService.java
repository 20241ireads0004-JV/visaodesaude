package br.com.ifba.relatorio.service;

import br.com.ifba.alerta.entity.Alerta;
import br.com.ifba.alerta.repository.AlertaRepository;
import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import br.com.ifba.projecaosaude.repository.ProjecaoSaudeRepository;
import br.com.ifba.relatorio.dto.RelatorioPostRequestDto;
import br.com.ifba.relatorio.dto.RelatorioGetResponseDto;
import br.com.ifba.relatorio.entity.Relatorio;
import br.com.ifba.relatorio.repository.RelatorioRepository;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;

import br.com.ifba.usuario.service.UsuarioService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService implements RelatorioIService {

    private final RelatorioRepository relatorioRepository;
    private final HabitoRepository habitoRepository;
    private final AlertaRepository alertaRepository;
    private final ProjecaoSaudeRepository projecaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    private static final String PASTA = System.getProperty("java.io.tmpdir")
            + File.separator + "visaohabitos";


    //  Gera o PDF e retorna os bytes para download direto

    @Override
    public byte[] gerarPdf(RelatorioPostRequestDto dto, Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        DadosRelatorio dados = coletarDados(dto, usuarioId);
        return construirPdf(dto, usuario, dados);
    }

    //  Salva o PDF em disco, gera token, retorna link válido por 72h
    @Override
    public RelatorioGetResponseDto compartilhar(RelatorioPostRequestDto dto, Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        byte[] pdfBytes = construirPdf(dto, usuario, coletarDados(dto, usuarioId));

        String token = UUID.randomUUID().toString();
        String caminho = salvarArquivo(pdfBytes, token);

        Relatorio relatorio = new Relatorio();
        relatorio.setTipo("LINK");
        relatorio.setPeriodo(dto.getPeriodo());
        relatorio.setDataGeracao(LocalDateTime.now());
        relatorio.setDataExpiracao(LocalDateTime.now().plusHours(72));
        relatorio.setLinkCompartilhamento(token);
        relatorio.setCaminhoArquivo(caminho);
        relatorio.setUsuarioId(usuarioId);
        Relatorio salvo = relatorioRepository.save(relatorio);

        String link = "/relatorios/download/" + token;
        return new RelatorioGetResponseDto(
                "LINK", dto.getPeriodo(),
                salvo.getDataGeracao(), link,
                salvo.getDataExpiracao(),
                "Link valido por 72 horas. Expira em: "
                        + salvo.getDataExpiracao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }


    //  downloadPorToken() — download via link compartilhado
    @Override
    public byte[] downloadPorToken(String token) {
        Relatorio relatorio = relatorioRepository.findByLinkCompartilhamento(token)
                .orElseThrow(() -> new BusinessException("Link nao encontrado ou invalido."));

        if (LocalDateTime.now().isAfter(relatorio.getDataExpiracao())) {
            throw new IllegalStateException("Este link expirou. Links compartilhaveis sao validos por 72 horas.");
        }

        try {
            return Files.readAllBytes(Path.of(relatorio.getCaminhoArquivo()));
        } catch (IOException e) {
            throw new BusinessException("Arquivo nao encontrado no servidor.");
        }
    }

    //  Coleta os dados do banco conforme período e seções selecionadas

    private DadosRelatorio coletarDados(RelatorioPostRequestDto dto, Long usuarioId) {
        LocalDate fim = LocalDate.now();
        LocalDate inicio = calcularInicio(dto.getPeriodo(), fim);

        Date dataInicio = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataFim = Date.from(fim.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Habito> habitos = habitoRepository
                .findByUsuario_IdAndDataBetweenOrderByDataAsc(usuarioId, dataInicio, dataFim);

        List<Alerta> alertas = dto.getSecoes().contains("ALERTAS")
                ? alertaRepository.findByUsuario_IdAndDataBetween(usuarioId, inicio, fim)
                : List.of();

        List<ProjecaoSaude> projecoes = dto.getSecoes().contains("PROJECOES")
                ? projecaoRepository.findByUsuario_Id(usuarioId)
                : List.of();
        return new DadosRelatorio(habitos, alertas, projecoes, inicio, fim);
    }

    //  Constrói o PDF com OpenPDF

    private byte[] construirPdf(RelatorioPostRequestDto dto, Usuario usuario, DadosRelatorio dados) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 60, 50);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            adicionarCabecalho(document, usuario, dto, dados);

            if (dto.getSecoes().contains("RESUMO")) {
                adicionarResumo(document, dados);
            }
            if (dto.getSecoes().contains("GRAFICOS")) {
                adicionarTendencias(document, dados);
            }
            if (dto.getSecoes().contains("ALERTAS")) {
                adicionarAlertas(document, dados.alertas());
            }
            if (dto.getSecoes().contains("PROJECOES")) {
                adicionarProjecoes(document, dados.projecoes());
            }
            adicionarRodape(document);
            document.close();
            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new BusinessException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    // Secão PDF

    private void adicionarCabecalho(Document doc, Usuario usuario,
                                    RelatorioPostRequestDto dto, DadosRelatorio dados) throws DocumentException {
        Color roxo = new Color(88, 81, 216);
        Color cinza = new Color(107, 114, 128);

        Font fonteTitulo = new Font(Font.HELVETICA, 22, Font.BOLD, roxo);
        Font fonteSubtitulo = new Font(Font.HELVETICA, 11, Font.NORMAL, cinza);
        Font fonteMeta = new Font(Font.HELVETICA, 9, Font.NORMAL, cinza);

        Paragraph titulo = new Paragraph("Visao de Habitos", fonteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulo);

        Paragraph subtitulo = new Paragraph("Relatorio de Saude — " + usuario.getNome(), fonteSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(4);
        doc.add(subtitulo);

        String periodoTexto = getPeriodoTexto(dto.getPeriodo());
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Paragraph meta = new Paragraph(
                "Periodo: " + periodoTexto + "  |  Gerado em: " + dataAtual, fonteMeta);
        meta.setAlignment(Element.ALIGN_CENTER);
        meta.setSpacingAfter(15);
        doc.add(meta);

        adicionarLinha(doc, roxo);
        doc.add(new Paragraph(" "));
    }

    private void adicionarResumo(Document doc, DadosRelatorio dados) throws DocumentException {
        Color roxo = new Color(88, 81, 216);
        Color roxoClaro = new Color(237, 236, 254);
        Color cinzaEscuro = new Color(55, 65, 81);
        Color verde = new Color(22, 163, 74);
        Color amarelo = new Color(202, 138, 4);
        Color vermelho = new Color(220, 38, 38);

        Font fonteSecao = new Font(Font.HELVETICA, 13, Font.BOLD, roxo);
        Font fonteCorpo = new Font(Font.HELVETICA, 10, Font.NORMAL, cinzaEscuro);

        Paragraph titulo = new Paragraph("Resumo Geral de Saude", fonteSecao);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(10);
        doc.add(titulo);

        if (dados.habitos().isEmpty()) {
            doc.add(new Paragraph("Nenhum registro encontrado para o periodo selecionado.", fonteCorpo));
            doc.add(new Paragraph(" "));
            return;
        }

        List<Habito> habitos = dados.habitos();
        double mediaSono = habitos.stream().mapToInt(Habito::getHorasSono).average().orElse(0);
        double mediaQualidade = habitos.stream().mapToInt(Habito::getQualidadeSono).average().orElse(0);
        double mediaExercicio = habitos.stream().mapToInt(Habito::getExercicioDuracao).average().orElse(0);
        double scoreMedio = calcularScore(mediaSono, mediaQualidade);
        int totalRegistros = habitos.size();

        // Tabela de métricas 2x2
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setSpacingAfter(15);

        adicionarCelulaMetrica(tabela, "Score Medio de Saude",
                String.format("%.0f/100", scoreMedio),
                scoreMedio >= 70 ? verde : scoreMedio >= 50 ? amarelo : vermelho,
                roxoClaro);

        adicionarCelulaMetrica(tabela, "Media de Horas de Sono",
                String.format("%.1fh", mediaSono),
                mediaSono >= 7 ? verde : mediaSono >= 6 ? amarelo : vermelho,
                roxoClaro);

        adicionarCelulaMetrica(tabela, "Qualidade do Sono",
                String.format("%.1f/5", mediaQualidade),
                mediaQualidade >= 4 ? verde : mediaQualidade >= 3 ? amarelo : vermelho,
                roxoClaro);

        adicionarCelulaMetrica(tabela, "Media de Exercicio",
                String.format("%.0f min/dia", mediaExercicio),
                mediaExercicio >= 30 ? verde : mediaExercicio >= 15 ? amarelo : vermelho,
                roxoClaro);

        doc.add(tabela);

        Paragraph totalP = new Paragraph("Total de registros no periodo: " + totalRegistros + " dias", fonteCorpo);
        totalP.setSpacingAfter(5);
        doc.add(totalP);

        adicionarLinha(doc, new Color(229, 231, 235));
        doc.add(new Paragraph(" "));
    }

    private void adicionarTendencias(Document doc, DadosRelatorio dados) throws DocumentException {
        Color roxo = new Color(88, 81, 216);
        Color roxoClaro = new Color(237, 236, 254);
        Color cinzaEscuro = new Color(55, 65, 81);


        Font fonteSecao = new Font(Font.HELVETICA, 13, Font.BOLD, roxo);
        Font fonteHeader = new Font(Font.HELVETICA, 9, Font.BOLD, roxo);
        Font fonteCorpo = new Font(Font.HELVETICA, 9, Font.NORMAL, cinzaEscuro);

        Paragraph titulo = new Paragraph("Graficos de Tendencia — Historico de Habitos", fonteSecao);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(10);
        doc.add(titulo);

        if (dados.habitos().isEmpty()) {
            Font fonteCorpoNormal = new Font(Font.HELVETICA, 10, Font.NORMAL, cinzaEscuro);
            doc.add(new Paragraph("Nenhum registro encontrado para o periodo selecionado.", fonteCorpoNormal));
            doc.add(new Paragraph(" "));
            return;
        }

        // Tabela de tendências
        PdfPTable tabela = new PdfPTable(5);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{2.5f, 1f, 1.2f, 1.2f, 1.5f});
        tabela.setSpacingAfter(15);

        // Cabeçalho
        String[] headers = {"Data", "Sono (h)", "Qualidade", "Exercicio (min)", "Score"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fonteHeader));
            cell.setBackgroundColor(roxoClaro);
            cell.setPadding(7);
            cell.setBorderColor(new Color(209, 213, 219));
            tabela.addCell(cell);
        }

        // Dados
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean linhaAlternada = false;
        for (Habito h : dados.habitos()) {
            Color bg = linhaAlternada ? new Color(249, 250, 251) : Color.WHITE;
            linhaAlternada = !linhaAlternada;

            LocalDate dataHabito = h.getData().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            double score = calcularScore(h.getHorasSono(), h.getQualidadeSono());

            adicionarCelulaDados(tabela, dataHabito.format(fmt), fonteCorpo, bg);
            adicionarCelulaDados(tabela, String.valueOf(h.getHorasSono()), fonteCorpo, bg);
            adicionarCelulaDados(tabela, h.getQualidadeSono() + "/5", fonteCorpo, bg);
            adicionarCelulaDados(tabela, String.valueOf(h.getExercicioDuracao()), fonteCorpo, bg);
            adicionarCelulaDados(tabela, String.format("%.0f", score), fonteCorpo, bg);
        }

        doc.add(tabela);
        adicionarLinha(doc, new Color(229, 231, 235));
        doc.add(new Paragraph(" "));
    }

    private void adicionarAlertas(Document doc, List<Alerta> alertas) throws DocumentException {
        Color roxo = new Color(88, 81, 216);
        Color cinzaEscuro = new Color(55, 65, 81);
        Color amarelo = new Color(202, 138, 4);


        Font fonteSecao = new Font(Font.HELVETICA, 13, Font.BOLD, roxo);
        Font fonteCorpo = new Font(Font.HELVETICA, 10, Font.NORMAL, cinzaEscuro);
        Font fonteTipo = new Font(Font.HELVETICA, 9, Font.BOLD, amarelo);
        Font fonteDesc = new Font(Font.HELVETICA, 9, Font.NORMAL, cinzaEscuro);

        Paragraph titulo = new Paragraph("Historico de Alertas", fonteSecao);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(10);
        doc.add(titulo);

        if (alertas.isEmpty()) {
            doc.add(new Paragraph("Nenhum alerta no periodo selecionado.", fonteCorpo));
            doc.add(new Paragraph(" "));
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        PdfPTable tabela = new PdfPTable(3);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1.5f, 2f, 5f});
        tabela.setSpacingAfter(15);

        // Cabeçalho
        Font fonteHeader = new Font(Font.HELVETICA, 9, Font.BOLD, roxo);
        Color roxoClaro = new Color(237, 236, 254);
        for (String h : new String[]{"Data", "Tipo", "Descricao"}) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fonteHeader));
            cell.setBackgroundColor(roxoClaro);
            cell.setPadding(7);
            tabela.addCell(cell);
        }

        // Dados
        boolean linhaAlternada = false;
        for (Alerta a : alertas) {
            Color bg = linhaAlternada ? new Color(249, 250, 251) : Color.WHITE;
            linhaAlternada = !linhaAlternada;
            adicionarCelulaDados(tabela, a.getData().format(fmt), fonteDesc, bg);
            adicionarCelulaDados(tabela, a.getTipo(), fonteTipo, bg);
            adicionarCelulaDados(tabela, a.getDescricao(), fonteDesc, bg);
        }

        doc.add(tabela);
        adicionarLinha(doc, new Color(229, 231, 235));
        doc.add(new Paragraph(" "));
    }

    private void adicionarProjecoes(Document doc, List<ProjecaoSaude> projecoes) throws DocumentException {
        Color roxo = new Color(88, 81, 216);
        Color cinzaEscuro = new Color(55, 65, 81);
        Color vermelho = new Color(220, 38, 38);
        Color amarelo = new Color(202, 138, 4);
        Color verde = new Color(22, 163, 74);

        Font fonteSecao = new Font(Font.HELVETICA, 13, Font.BOLD, roxo);
        Font fonteCorpo = new Font(Font.HELVETICA, 10, Font.NORMAL, cinzaEscuro);

        Paragraph titulo = new Paragraph("Projecoes a Longo Prazo", fonteSecao);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(10);
        doc.add(titulo);

        if (projecoes.isEmpty()) {
            doc.add(new Paragraph("Nenhuma projecao disponivel.", fonteCorpo));
            doc.add(new Paragraph(" "));
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (ProjecaoSaude p : projecoes) {
            Color riscoColor = p.getRiscoCardioVascular() >= 70 ? vermelho
                    : p.getRiscoCardioVascular() >= 40 ? amarelo : verde;
            String riscoLabel = p.getRiscoCardioVascular() >= 70 ? "ALTO"
                    : p.getRiscoCardioVascular() >= 40 ? "MODERADO" : "BAIXO";

            PdfPTable card = new PdfPTable(2);
            card.setWidthPercentage(100);
            card.setWidths(new float[]{3f, 1f});
            card.setSpacingAfter(8);

            // Descrição
            Font fonteDesc = new Font(Font.HELVETICA, 10, Font.NORMAL, cinzaEscuro);
            PdfPCell cellDesc = new PdfPCell(new Phrase(p.getDescricao() + "\nData: " + p.getData().format(fmt), fonteDesc));
            cellDesc.setPadding(10);
            cellDesc.setBorderColor(new Color(229, 231, 235));
            card.addCell(cellDesc);

            // Risco cardiovascular
            Font fonteRisco = new Font(Font.HELVETICA, 11, Font.BOLD, riscoColor);
            PdfPCell cellRisco = new PdfPCell(new Phrase(
                    "Risco Cardio\n" + p.getRiscoCardioVascular() + "%\n" + riscoLabel, fonteRisco));
            cellRisco.setPadding(10);
            cellRisco.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellRisco.setBorderColor(new Color(229, 231, 235));
            card.addCell(cellRisco);

            doc.add(card);
        }

        doc.add(new Paragraph(" "));
    }

    private void adicionarRodape(Document doc) throws DocumentException {
        Color cinza = new Color(156, 163, 175);
        Font fonteRodape = new Font(Font.HELVETICA, 8, Font.NORMAL, cinza);

        adicionarLinha(doc, cinza);

        Paragraph rodape = new Paragraph(
                "Gerado automaticamente pelo sistema Visao de Habitos  |  "
                        + "Links compartilhaveis expiram em 72 horas conforme LGPD  |  "
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                fonteRodape);
        rodape.setAlignment(Element.ALIGN_CENTER);
        rodape.setSpacingBefore(8);
        doc.add(rodape);
    }


    //  Métodos auxiliares


    private void adicionarCelulaMetrica(PdfPTable tabela, String label, String valor,
                                        Color corValor, Color bgColor) {
        Font fonteLabel = new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(107, 114, 128));
        Font fonteValor = new Font(Font.HELVETICA, 16, Font.BOLD, corValor);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bgColor);
        cell.setPadding(12);
        cell.setBorderColor(new Color(209, 213, 219));
        cell.addElement(new Paragraph(label, fonteLabel));
        cell.addElement(new Paragraph(valor, fonteValor));
        tabela.addCell(cell);
    }

    private void adicionarCelulaDados(PdfPTable tabela, String texto, Font fonte, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setPadding(6);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(new Color(229, 231, 235));
        tabela.addCell(cell);
    }

    private void adicionarLinha(Document doc, Color cor) throws DocumentException {
        PdfPTable linha = new PdfPTable(1);
        linha.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorderWidthBottom(1f);
        cell.setBorderColor(cor);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setMinimumHeight(1f);
        linha.addCell(cell);
        doc.add(linha);
    }

    private String salvarArquivo(byte[] bytes, String token) {
        try {
            File pasta = new File(PASTA);
            if (!pasta.exists()) pasta.mkdirs();
            String caminho = PASTA + File.separator + token + ".pdf";
            Files.write(Path.of(caminho), bytes);
            return caminho;
        } catch (IOException e) {
            throw new BusinessException("Erro ao salvar arquivo PDF: " + e.getMessage(), e);
        }
    }

    private LocalDate calcularInicio(String periodo, LocalDate fim) {
        return switch (periodo) {
            case "7_DIAS" -> fim.minusDays(7);
            case "3_MESES" -> fim.minusMonths(3);
            case "1_ANO" -> fim.minusYears(1);
            default -> fim.minusDays(30);
        };
    }

    private String getPeriodoTexto(String periodo) {
        return switch (periodo) {
            case "7_DIAS" -> "Ultimos 7 dias";
            case "30_DIAS" -> "Ultimos 30 dias";
            case "3_MESES" -> "Ultimos 3 meses";
            case "1_ANO" -> "Ultimo ano";
            default -> periodo;
        };
    }

    /**
     * Score = 60% sono (ideal=8h) + 40% qualidade do sono (escala 1-5)
     */
    private double calcularScore(double mediaSono, double mediaQualidade) {
        return (Math.min(mediaSono / 8.0, 1.0) * 60) + ((mediaQualidade / 5.0) * 40);
    }

    /**
     * Record interno para carregar dados coletados
     */
    private record DadosRelatorio(
            List<Habito> habitos,
            List<Alerta> alertas,
            List<ProjecaoSaude> projecoes,
            LocalDate inicio,
            LocalDate fim
    ) {

    }


}