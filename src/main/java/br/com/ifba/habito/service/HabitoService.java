package br.com.ifba.habito.service;

import br.com.ifba.alerta.entity.Alerta;
import br.com.ifba.alerta.repository.AlertaRepository;
import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.repository.HabitoRepository;
import br.com.ifba.infraestructure.exception.ResourceNotFoundException;
import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import br.com.ifba.projecaosaude.repository.ProjecaoSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitoService implements HabitoIService {

    private final HabitoRepository habitoRepository;
    private final AlertaRepository alertaRepository;
    private final ProjecaoSaudeRepository projecaoSaudeRepository; // 1. Repositório adicionado

    @Override
    public Habito save(Habito habito) {

        Habito habitoSalvo = habitoRepository.save(habito);

        gerarAlertas(habitoSalvo);
        gerarProjecao(habitoSalvo); // 2. Chamada para gerar a projeção automaticamente

        return habitoSalvo;
    }

    // 3. Método que cria a projeção de saúde baseada no hábito
    private void gerarProjecao(Habito habito) {
        ProjecaoSaude projecao = new ProjecaoSaude();
        projecao.setUsuario(habito.getUsuario());
        projecao.setData(java.time.LocalDate.now());
        projecao.setVisualizacao(false);

        // Cálculo base simulado (20 é o ideal que definimos antes)
        int riscoCalculado = 20;

        // Penalidades por hábitos ruins
        if (habito.getHorasSono() < 6) riscoCalculado += 5;
        if (habito.getExercicioDuracao() < 30) riscoCalculado += 10;
        if (habito.getAguaCopos() < 5) riscoCalculado += 5;
        if (habito.getAlimentacao().toLowerCase().contains("ultraprocessados")) riscoCalculado += 10;

        // IMC
        if (habito.getAltura() > 0) {
            double imc = habito.getPeso() / (habito.getAltura() * habito.getAltura());
            if (imc > 25.0) riscoCalculado += 10;
        }

        projecao.setRiscoCardioVascular(riscoCalculado);
        projecao.setDescricao("Projeção gerada automaticamente a partir do último registro de hábitos.");

        projecaoSaudeRepository.save(projecao);
    }

    private void gerarAlertas(Habito habito) {

        if (habito.getHorasSono() < 6) {
            alertaRepository.save(
                    criarAlerta(habito, "ALTO", "Você dormiu menos de 6 horas.")
            );
        }

        if (habito.getQualidadeSono() <= 2) {
            alertaRepository.save(
                    criarAlerta(habito, "MODERADO", "Sua qualidade do sono foi baixa.")
            );
        }

        if (habito.getAguaCopos() < 6) {
            alertaRepository.save(
                    criarAlerta(habito, "MODERADO", "Você bebeu pouca água.")
            );
        }

        if (habito.getExercicioDuracao() < 30) {
            alertaRepository.save(
                    criarAlerta(habito, "ALTO", "Você realizou pouca atividade física.")
            );
        }

        if (habito.getAlimentacao().equalsIgnoreCase("Ruim")) {
            alertaRepository.save(
                    criarAlerta(habito, "ALTO", "Sua alimentação precisa melhorar.")
            );
        }
    }

    private Alerta criarAlerta(Habito habito, String tipo, String descricao) {
        Alerta alerta = new Alerta();
        alerta.setTipo(tipo);
        alerta.setDescricao(descricao);
        alerta.setData(java.time.LocalDate.now());
        alerta.setVisualizacao(false);
        alerta.setUsuario(habito.getUsuario());
        return alerta;
    }

    @Override
    public Page<Habito> findAll(Pageable pageable) {
        return habitoRepository.findAll(pageable);
    }

    @Override
    public Habito findById(Long id) {
        return habitoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito não encontrado com id: " + id));
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
        habitoAtual.setAguaCopos(habito.getAguaCopos());
        habitoAtual.setPeso(habito.getPeso());
        habitoAtual.setAltura(habito.getAltura());
        return habitoRepository.save(habitoAtual);
    }

    @Override
    public Habito buscarHabitoHoje(Long usuarioId) {
        return habitoRepository
                .findTopByUsuarioIdOrderByIdDesc(usuarioId)
                .orElse(null);
    }
}