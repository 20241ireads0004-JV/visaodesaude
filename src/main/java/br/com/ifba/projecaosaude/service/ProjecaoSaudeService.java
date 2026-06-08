package br.com.ifba.projecaosaude.service;

import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import br.com.ifba.projecaosaude.repository.ProjecaoSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjecaoSaudeService implements ProjecaoSaudeIService {

    private final ProjecaoSaudeRepository projecaoSaudeRepository;

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
}