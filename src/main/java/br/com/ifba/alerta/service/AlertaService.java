package br.com.ifba.alerta.service;

import br.com.ifba.alerta.entity.Alerta;
import br.com.ifba.alerta.repository.AlertaRepository;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertaService implements AlertaIService {

    private final AlertaRepository alertaRepository;

    @Override
    public Alerta save(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    @Override
    public Page<Alerta> findAll(Pageable pageable) {
        return alertaRepository.findAll(pageable);
    }

    @Override
    public Alerta findById(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Alerta não encontrado com o ID: " + id));
    }

    @Override
    public void delete(Long id) {

        Alerta alerta = findById(id);

        alertaRepository.delete(alerta);
    }

    @Override
    public Alerta update(Long id, Alerta alerta) {

        Alerta alertaAtual = findById(id);

        alertaAtual.setTipo(alerta.getTipo());
        alertaAtual.setDescricao(alerta.getDescricao());
        alertaAtual.setData(alerta.getData());
        alertaAtual.setVisualizacao(alerta.getVisualizacao());

        return alertaRepository.save(alertaAtual);
    }
}
