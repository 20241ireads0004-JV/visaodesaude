package br.com.ifba.projecaosaude.service;

import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
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
    public ProjecaoSaude minhaProjecao(Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado. ID: " + usuarioId));

        List<ProjecaoSaude> projecoes = projecaoSaudeRepository.findByUsuario_Id(usuarioId);

        if (projecoes.isEmpty()) {
            throw new BusinessException("Nenhuma projeção de saúde encontrada para este usuário.");
        }

        return (ProjecaoSaude) projecoes;
    }

    @Override
    public String comparar(Long usuarioId) {

        List<ProjecaoSaude> projecoes = Collections.singletonList(minhaProjecao(usuarioId));

        // Considera a projeção mais recente
        ProjecaoSaude projecaoAtual = projecoes.get(projecoes.size() - 1);

        int riscoAtual = projecaoAtual.getRiscoCardioVascular();
        int riscoIdeal = 20;

        if (riscoAtual == riscoIdeal) {
            return "Parabéns! Sua projeção está no nível considerado ideal.";
        }

        if (riscoAtual > riscoIdeal) {
            return "Seu risco cardiovascular está " +
                    (riscoAtual - riscoIdeal) +
                    " pontos acima do ideal.";
        }

        return "Seu risco cardiovascular está " +
                (riscoIdeal - riscoAtual) +
                " pontos abaixo do valor de referência.";
    }

    @Override
    public List<String> recomendacoes(Long usuarioId) {

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("Usuário não encontrado."));

        Habito habito = habitoRepository.findFirstByUsuarioIdOrderByDataDesc(usuarioId)
                .orElseThrow(() ->
                        new BusinessException("O usuário não possui hábitos cadastrados."));

        List<String> recomendacoes = new ArrayList<>();

        // Sono
        if (habito.getHorasSono() < 7) {
            recomendacoes.add("Procure dormir entre 7 e 9 horas por noite.");
        }

        if (habito.getQualidadeSono() < 7) {
            recomendacoes.add("Crie uma rotina de sono, evitando telas antes de dormir.");
        }

        // Alimentação
        if (!habito.getAlimentacao().equalsIgnoreCase("Saudável")) {
            recomendacoes.add("Prefira uma alimentação rica em frutas, verduras e legumes.");
            recomendacoes.add("Reduza o consumo de alimentos ultraprocessados.");
        }

        // Exercícios
        if (habito.getExercicioDuracao() < 30) {
            recomendacoes.add("Pratique pelo menos 30 minutos de atividade física diariamente.");
        }

        if (habito.getExercicioIntensidade() < 5) {
            recomendacoes.add("Aumente gradualmente a intensidade dos exercícios, respeitando seus limites.");
        }

        if (habito.getExercicioTipo() == 0) {
            recomendacoes.add("Inclua caminhadas, corridas, ciclismo ou musculação na sua rotina.");
        }

        if (recomendacoes.isEmpty()) {
            recomendacoes.add("Parabéns! Seus hábitos estão alinhados com um estilo de vida saudável.");
            recomendacoes.add("Continue mantendo uma alimentação equilibrada.");
            recomendacoes.add("Mantenha sua rotina de exercícios físicos.");
        }

        return recomendacoes;
    }
}