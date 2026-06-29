package br.com.ifba.alerta.service;

import br.com.ifba.alerta.entity.Alerta;
import br.com.ifba.alerta.repository.AlertaRepository;
import br.com.ifba.infraestructure.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertaService implements AlertaIService {

    private final AlertaRepository alertaRepository;

    @Override
    @Transactional
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
                        new BusinessException("Alerta não encontrado. ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!alertaRepository.existsById(id)) {
            throw new BusinessException("Alerta não encontrado. ID: " + id);
        }

        alertaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Alerta update(Long id, Alerta alerta) {

        Alerta alertaExistente = alertaRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Alerta não encontrado. ID: " + id));

        alertaExistente.setTipo(alerta.getTipo());
        alertaExistente.setDescricao(alerta.getDescricao());
        alertaExistente.setData(alerta.getData());
        alertaExistente.setVisualizacao(alerta.getVisualizacao());

        return alertaRepository.save(alertaExistente);
    }
}