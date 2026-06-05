package br.com.ifba.habito.service;

import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitoService implements HabitoIService {

    private final HabitoRepository habitoRepository;

    @Override
    public Habito save(Habito habito) {
        return habitoRepository.save(habito);
    }

    @Override
    public Page<Habito> findAll(Pageable pageable) {
        return habitoRepository.findAll(pageable);
    }

    @Override
    public Habito findById(Long id) {
        return habitoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hábito não encontrado com id: " + id));
    }

    @Override
    public void delete(Long id) {
        Habito habito = this.findById(id);
        habitoRepository.delete(habito);
    }

    @Override
    public Habito update(Long id, Habito habito) {

        Habito habitoAtual = this.findById(id);

        habitoAtual.setData(habito.getData());
        habitoAtual.setHorasSono(habito.getHorasSono());
        habitoAtual.setQualidadeSono(habito.getQualidadeSono());
        habitoAtual.setAlimentacao(habito.getAlimentacao());
        habitoAtual.setExercicioIntensidade(habito.getExercicioIntensidade());
        habitoAtual.setExercicioTipo(habito.getExercicioTipo());
        habitoAtual.setExercicioDuracao(habito.getExercicioDuracao());

        return habitoRepository.save(habitoAtual);
    }
}