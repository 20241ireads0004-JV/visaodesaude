package br.com.ifba.projecaosaude.service;

import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import br.com.ifba.projecaosaude.dto.ComparacaoResponseDto;
import br.com.ifba.projecaosaude.dto.RecomendacaoResponseDto;
import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import br.com.ifba.projecaosaude.repository.ProjecaoSaudeRepository;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjecaoSaudeService implements ProjecaoSaudeIService {

    private final ProjecaoSaudeRepository projecaoSaudeRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitoRepository habitoRepository;

    @Override
    public ProjecaoSaude save(ProjecaoSaude projecaoSaude) {
        return projecaoSaudeRepository.save(projecaoSaude);
    }

    @Override
    public Page<ProjecaoSaude> findAll(Pageable pageable) {
        return projecaoSaudeRepository.findAll(pageable);
    }

    @Override
    public ProjecaoSaude findById(Long id) {
        return projecaoSaudeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Projeção de Saúde não encontrada com o ID: " + id));
    }

    @Override
    public void delete(Long id) {

        ProjecaoSaude projecaoSaude = findById(id);

        projecaoSaudeRepository.delete(projecaoSaude);
    }

    @Override
    public ProjecaoSaude update(Long id, ProjecaoSaude projecaoSaude) {

        ProjecaoSaude projecaoSaudeAtual = findById(id);

        projecaoSaudeAtual.setRiscoCardioVascular(
                projecaoSaude.getRiscoCardioVascular());

        projecaoSaudeAtual.setDescricao(
                projecaoSaude.getDescricao());

        projecaoSaudeAtual.setData(
                projecaoSaude.getData());

        projecaoSaudeAtual.setVisualizacao(
                projecaoSaude.getVisualizacao());

        return projecaoSaudeRepository.save(projecaoSaudeAtual);
    }

    @Override
    public List<ProjecaoSaude> minhaProjecao(Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado. ID: " + usuarioId));

        List<ProjecaoSaude> projecoes =
                projecaoSaudeRepository.findByUsuario_Id(usuarioId);

        if (projecoes.isEmpty()) {
            throw new BusinessException("Nenhuma projeção encontrada.");
        }

        return projecoes;
    }

    @Override
    public ComparacaoResponseDto comparar(Long usuarioId) {

        List<ProjecaoSaude> projecoes = minhaProjecao(usuarioId);

        ProjecaoSaude projecao = projecoes.get(projecoes.size() - 1);

        int riscoAtual = projecao.getRiscoCardioVascular();

        int riscoIdeal = 20;

        String mensagem;

        if (riscoAtual == riscoIdeal) {
            mensagem = "Parabéns! Sua projeção está no nível ideal.";
        } else if (riscoAtual > riscoIdeal) {
            mensagem = "Seu risco está " + (riscoAtual - riscoIdeal) + " pontos acima do ideal.";
        } else {
            mensagem = "Seu risco está " + (riscoIdeal - riscoAtual) + " pontos abaixo do valor de referência.";
        }

        return new ComparacaoResponseDto(
                riscoAtual,
                riscoIdeal,
                Math.abs(riscoAtual - riscoIdeal),
                mensagem
        );
    }

    @Override
    public List<RecomendacaoResponseDto> recomendacoes(Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado."));

        Habito habito = habitoRepository
                .findFirstByUsuarioIdOrderByDataDesc(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("O usuário não possui hábitos cadastrados."));

        List<RecomendacaoResponseDto> recomendacoes = new ArrayList<>();

        // SONO
        if (habito.getHorasSono() < 7) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Sono",
                            "Durma mais",
                            "Procure dormir entre 7 e 9 horas por noite.",
                            "ALTA",
                            "ALTO"
                    )
            );
        }

        if (habito.getQualidadeSono() < 7) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Sono",
                            "Melhore a qualidade do sono",
                            "Crie uma rotina de sono evitando telas antes de dormir.",
                            "MEDIA",
                            "MEDIO"
                    )
            );
        }

        // ALIMENTAÇÃO

        if (!habito.getAlimentacao().equalsIgnoreCase("Saudável")) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Alimentacao",
                            "Melhore sua alimentação",
                            "Prefira frutas, verduras e legumes.",
                            "ALTA",
                            "ALTO"
                    )
            );

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Alimentacao",
                            "Evite ultraprocessados",
                            "Reduza alimentos industrializados.",
                            "MEDIA",
                            "MEDIO"
                    )
            );
        }

        // EXERCÍCIOS

        if (habito.getExercicioDuracao() < 30) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Exercicio",
                            "Aumente o tempo de atividade",
                            "Pratique pelo menos 30 minutos diariamente.",
                            "ALTA",
                            "ALTO"
                    )
            );
        }

        if (habito.getExercicioIntensidade() < 5) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Exercicio",
                            "Aumente a intensidade",
                            "Aumente gradualmente a intensidade dos exercícios.",
                            "MEDIA",
                            "MEDIO"
                    )
            );
        }

        if (habito.getExercicioTipo() == 0) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Exercicio",
                            "Inclua atividades físicas",
                            "Inclua caminhada, corrida, ciclismo ou musculação.",
                            "BAIXA",
                            "MEDIO"
                    )
            );
        }

        if (recomendacoes.isEmpty()) {

            recomendacoes.add(
                    new RecomendacaoResponseDto(
                            "Saúde",
                            "Excelente trabalho",
                            "Parabéns! Continue mantendo seus hábitos saudáveis.",
                            "BAIXA",
                            "BAIXO"
                    )
            );
        }

        return recomendacoes;
    }

    @Override
    public Double calcularImc(Long usuarioId) {

        Habito habito = habitoRepository
                .findFirstByUsuarioIdOrderByDataDesc(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Nenhum hábito encontrado."));

        double imc =
                habito.getPeso() /
                        (habito.getAltura() * habito.getAltura());

        return Math.round(imc * 10.0) / 10.0;
    }

    @Override
    public String qualidadeSono(Long usuarioId) {

        Habito habito = habitoRepository
                .findFirstByUsuarioIdOrderByDataDesc(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Nenhum hábito encontrado."));

        if (habito.getQualidadeSono() >= 3) {
            return "Boa";
        }

        return "Ruim";
    }

    @Override
    public String energia(Long usuarioId) {

        Habito habito = habitoRepository
                .findFirstByUsuarioIdOrderByDataDesc(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Nenhum hábito encontrado."));

        if (habito.getExercicioDuracao() >= 60) {
            return "Alta";
        }

        if (habito.getExercicioDuracao() >= 30) {
            return "Média";
        }

        return "Baixa";
    }

}