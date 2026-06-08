package br.com.ifba.cenario.service;

import br.com.ifba.cenario.entity.Cenario;
import br.com.ifba.cenario.repository.CenarioRepository;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CenarioService implements CenarioIService {

    private final CenarioRepository cenarioRepository;

    @Override
    public Cenario save(Cenario cenario) {
        return cenarioRepository.save(cenario);
    }

    @Override
    public Page<Cenario> findAll(Pageable pageable) {
        return cenarioRepository.findAll(pageable);
    }

    @Override
    public Cenario findById(Long id) {
        return cenarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cenário não encontrado com o ID: " + id));
    }

    @Override
    public void delete(Long id) {

        Cenario cenario = findById(id);

        cenarioRepository.delete(cenario);
    }

    @Override
    public Cenario update(Long id, Cenario cenario) {

        Cenario cenarioAtual = findById(id);

        cenarioAtual.setTipo(cenario.getTipo());
        cenarioAtual.setHorasSono(cenario.getHorasSono());
        cenarioAtual.setAlimentacao(cenario.getAlimentacao());
        cenarioAtual.setExercicio(cenario.getExercicio());

        return cenarioRepository.save(cenarioAtual);
    }
}